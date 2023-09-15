package com.yanggu.metric_calculate.flink;


import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core.kryo.serializer.acc.*;
import com.yanggu.metric_calculate.core.kryo.serializer.cube.DimensionSetSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.cube.MetricCubeSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoCollectionSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoMapSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoTreeMapSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.window.*;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.util.KeyValue;
import com.yanggu.metric_calculate.core.window.*;
import com.yanggu.metric_calculate.flink.pojo.DeriveCalculateData;
import com.yanggu.metric_calculate.flink.pojo.DeriveConfigData;
import com.yanggu.metric_calculate.flink.process_function.DataTableProcessFunction;
import com.yanggu.metric_calculate.flink.process_function.KeyedBatchDeriveBroadcastProcessFunction;
import com.yanggu.metric_calculate.flink.process_function.MetricDataMetricConfigBroadcastProcessFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;
import org.dromara.hutool.core.collection.queue.BoundedPriorityQueue;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.core.lang.tuple.Tuple;
import org.dromara.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE;
import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE_CONFIG;
import static com.yanggu.metric_calculate.flink.util.DeriveMetricCalculateUtil.deriveMapStateDescriptor;
import static org.apache.flink.api.common.RuntimeExecutionMode.BATCH;

/**
 * 指标批计算flink程序
 * <p>需要重新编写源和处理函数</p>
 * <p>如果一个作业有多个源, 只要有一个源是无界的, 那么该作业是流作业</p>
 * <p>否则是批作业</p>
 */
public class MetricCalculateFlinkBatchJob {

    public static void main(String[] args) throws Exception {
        //启动一个webUI，指定本地WEB-UI端口号
        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, 8081);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        //批处理模式
        env.setRuntimeMode(BATCH);
        env.setParallelism(1);

        //强制使用kryo
        env.getConfig().enableForceKryo();

        //添加Table序列化和反序列化器
        env.registerTypeWithKryoSerializer(TumblingTimeWindow.class, new TumblingTimeWindowSerializer<>());
        env.registerTypeWithKryoSerializer(GlobalWindow.class, new GlobalWindowSerializer<>());
        env.registerTypeWithKryoSerializer(SlidingTimeWindow.class, new SlidingTimeWindowSerializer<>());
        env.registerTypeWithKryoSerializer(SlidingCountWindow.class, new SlidingCountWindowSerializer<>());
        env.registerTypeWithKryoSerializer(StatusWindow.class, new StatusWindowSerializer<>());
        env.registerTypeWithKryoSerializer(PatternWindow.class, new PatternWindowSerializer<>());

        //ACC序列化器和反序列化器
        env.registerTypeWithKryoSerializer(Tuple.class, new TupleSerializer());
        env.registerTypeWithKryoSerializer(MutableEntrySerializer.class, new MutableEntrySerializer<>());
        env.registerTypeWithKryoSerializer(BoundedPriorityQueue.class, new BoundedPriorityQueueSerializer<>());
        env.registerTypeWithKryoSerializer(MutableObj.class, new MutableObjectSerializer<>());
        env.registerTypeWithKryoSerializer(Pair.class, new PairSerializer<>());
        env.registerTypeWithKryoSerializer(MultiFieldDistinctKey.class, new MultiFieldDistinctKeySerializer());
        env.registerTypeWithKryoSerializer(MultiFieldOrderCompareKey.class, new MultiFieldOrderCompareKeySerializer());
        env.registerTypeWithKryoSerializer(KeyValue.class, new KeyValueSerializer<>());
        env.registerTypeWithKryoSerializer(ArrayList.class, new KryoCollectionSerializer<ArrayList<Object>>());
        env.registerTypeWithKryoSerializer(TreeMap.class, new KryoTreeMapSerializer());
        env.registerTypeWithKryoSerializer(HashMap.class, new KryoMapSerializer<HashMap<Object, Object>>());

        //MetricCube序列化器和反序列化器
        env.registerTypeWithKryoSerializer(DimensionSet.class, new DimensionSetSerializer());
        env.registerTypeWithKryoSerializer(MetricCube.class, new MetricCubeSerializer<>());

        //数据明细宽表配置流
        String jsonString = FileUtil.readUtf8String("mock_metric_config/1.json");
        DataStreamSource<Model> tableSourceDataStream = env.fromElements(JSONUtil.toBean(jsonString, Model.class));

        //分流出派生指标配置流和全局指标配置流
        SingleOutputStreamOperator<Void> tableConfigDataStream = tableSourceDataStream.process(new DataTableProcessFunction());

        //派生指标配置数据流
        BroadcastStream<DeriveConfigData> deriveBroadcastStream = tableConfigDataStream
                .getSideOutput(new OutputTag<>(DERIVE_CONFIG, TypeInformation.of(DeriveConfigData.class)))
                .broadcast(deriveMapStateDescriptor);

        //将数据明细宽表数据流进行广播
        BroadcastStream<Model> tableSourceBroadcast = tableSourceDataStream
                .broadcast(new MapStateDescriptor<>("Model", Long.class, Model.class));

        List<String> jsonList = FileUtil.readUtf8Lines("data.txt");

        env
                //宽表数据流
                .fromCollection(jsonList)
                .connect(tableSourceBroadcast)
                //分流出派生指标数据流和全局指标数据流
                .process(new MetricDataMetricConfigBroadcastProcessFunction())
                .getSideOutput(new OutputTag<>(DERIVE, TypeInformation.of(DeriveCalculateData.class)))
                //根据DimensionSet进行keyBy
                .keyBy(DeriveCalculateData::getDimensionSet)
                //连接派生指标配置流
                .connect(deriveBroadcastStream)
                .process(new KeyedBatchDeriveBroadcastProcessFunction())
                .print("print>>>>");

        env.execute("批指标计算服务");
    }

}
