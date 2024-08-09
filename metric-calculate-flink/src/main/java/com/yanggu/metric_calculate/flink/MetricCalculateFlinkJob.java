package com.yanggu.metric_calculate.flink;


import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.kryo.serializer.acc.BoundedPriorityQueueSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.acc.MultiFieldDataSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.acc.MutableObjectSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.acc.MutablePairSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.acc.PairSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.acc.TupleSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.cube.DimensionSetSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.cube.MetricCubeSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoCollectionSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoMapSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoTreeMapSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.window.GlobalWindowSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.window.PatternWindowSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.window.SlidingCountWindowSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.window.SlidingTimeWindowSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.window.StatusWindowSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.window.TumblingTimeWindowSerializer;
import com.yanggu.metric_calculate.core.pojo.acc.BoundedPriorityQueue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.window.GlobalWindow;
import com.yanggu.metric_calculate.core.window.PatternWindow;
import com.yanggu.metric_calculate.core.window.SlidingCountWindow;
import com.yanggu.metric_calculate.core.window.SlidingTimeWindow;
import com.yanggu.metric_calculate.core.window.StatusWindow;
import com.yanggu.metric_calculate.core.window.TumblingTimeWindow;
import com.yanggu.metric_calculate.flink.operator.NoKeyProcessTimeMiniBatchOperator;
import com.yanggu.metric_calculate.flink.pojo.DeriveCalculateData;
import com.yanggu.metric_calculate.flink.pojo.DeriveConfigData;
import com.yanggu.metric_calculate.flink.process_function.BatchReadProcessFunction;
import com.yanggu.metric_calculate.flink.process_function.BatchWriteProcessFunction;
import com.yanggu.metric_calculate.flink.process_function.DataTableProcessFunction;
import com.yanggu.metric_calculate.flink.process_function.DeriveBroadcastProcessFunction;
import com.yanggu.metric_calculate.flink.process_function.KeyedDeriveBroadcastProcessFunction;
import com.yanggu.metric_calculate.flink.process_function.MetricDataMetricConfigBroadcastProcessFunction;
import com.yanggu.metric_calculate.flink.source_function.TableDataSourceFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.core.lang.tuple.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE;
import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE_CONFIG;
import static com.yanggu.metric_calculate.flink.util.DeriveMetricCalculateUtil.deriveMapStateDescriptor;

/**
 * 指标计算flink程序
 */
public class MetricCalculateFlinkJob {

    public static void main(String[] args) throws Exception {
        //启动一个webUI，指定本地WEB-UI端口号
        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, 8081);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
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
        env.registerTypeWithKryoSerializer(MutablePairSerializer.class, new MutablePairSerializer<>());
        env.registerTypeWithKryoSerializer(BoundedPriorityQueue.class, new BoundedPriorityQueueSerializer<>());
        env.registerTypeWithKryoSerializer(MutableObj.class, new MutableObjectSerializer<>());
        env.registerTypeWithKryoSerializer(Pair.class, new PairSerializer<>());
        env.registerTypeWithKryoSerializer(MultiFieldData.class, new MultiFieldDataSerializer());
        env.registerTypeWithKryoSerializer(Pair.class, new PairSerializer<>());
        env.registerTypeWithKryoSerializer(ArrayList.class, new KryoCollectionSerializer<ArrayList<Object>>());
        env.registerTypeWithKryoSerializer(TreeMap.class, new KryoTreeMapSerializer());
        env.registerTypeWithKryoSerializer(HashMap.class, new KryoMapSerializer<HashMap<Object, Object>>());

        //MetricCube序列化器和反序列化器
        env.registerTypeWithKryoSerializer(DimensionSet.class, new DimensionSetSerializer());
        env.registerTypeWithKryoSerializer(MetricCube.class, new MetricCubeSerializer<>());

        //数据明细宽表配置流
        DataStreamSource<Model> tableSourceDataStream = env.addSource(new TableDataSourceFunction(), "Table-Source");

        //分流出派生指标配置流和全局指标配置流
        SingleOutputStreamOperator<Void> tableConfigDataStream = tableSourceDataStream.process(new DataTableProcessFunction());

        //派生指标配置数据流
        BroadcastStream<DeriveConfigData> deriveBroadcastStream = tableConfigDataStream
                .getSideOutput(new OutputTag<>(DERIVE_CONFIG, TypeInformation.of(DeriveConfigData.class)))
                .broadcast(deriveMapStateDescriptor);

        //将数据明细宽表数据流进行广播
        BroadcastStream<Model> tableSourceBroadcast = tableSourceDataStream
                .broadcast(new MapStateDescriptor<>("Model", Long.class, Model.class));

        //KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
        //        .setBootstrapServers("172.20.7.143:9092")
        //        .setGroupId("metric-calculate")
        //        .setTopics("metric-calculate")
        //        .build();

        SingleOutputStreamOperator<Void> dataStream = env
                //.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka-Source")
                //宽表数据流
                .socketTextStream("localhost", 6666)
                .connect(tableSourceBroadcast)
                //分流出派生指标数据流和全局指标数据流
                .process(new MetricDataMetricConfigBroadcastProcessFunction());

        //攒批读组件
        TypeInformation<DeriveCalculateData> deriveCalculateDataTypeInformation = TypeInformation.of(DeriveCalculateData.class);
        NoKeyProcessTimeMiniBatchOperator<DeriveCalculateData> deriveNoKeyProcessTimeMiniBatchOperator = new NoKeyProcessTimeMiniBatchOperator<>();
        deriveNoKeyProcessTimeMiniBatchOperator.setElementTypeInfo(deriveCalculateDataTypeInformation);
        BatchReadProcessFunction batchReadProcessFunction = new BatchReadProcessFunction();

        //攒批写组件
        NoKeyProcessTimeMiniBatchOperator<MetricCube> deriveNoKeyProcessTimeMiniBatchOperator2 = new NoKeyProcessTimeMiniBatchOperator<>();
        deriveNoKeyProcessTimeMiniBatchOperator2.setElementTypeInfo(TypeInformation.of(MetricCube.class));

        //派生指标数据流
        dataStream
                //分流出派生指标数据
                .getSideOutput(new OutputTag<>(DERIVE, deriveCalculateDataTypeInformation))
                //攒批读组件
                .transform("DeriveMetrics Batch Read Operator", TypeInformation.of(new TypeHint<>() {
                }), deriveNoKeyProcessTimeMiniBatchOperator)
                //批读
                .process(batchReadProcessFunction)
                //根据维度进行keyBy
                .keyBy(DeriveCalculateData::getDimensionSet)
                //连接派生指标配置流
                .connect(deriveBroadcastStream)
                //计算派生指标
                .process(new KeyedDeriveBroadcastProcessFunction())
                //攒批写组件
                .transform("DeriveMetrics Batch Update Operator", TypeInformation.of(new TypeHint<>() {
                }), deriveNoKeyProcessTimeMiniBatchOperator2)
                //批写
                .process(new BatchWriteProcessFunction())
                //连接派生指标配置流
                .connect(deriveBroadcastStream)
                //返回计算后的数据
                .process(new DeriveBroadcastProcessFunction())
                .print("derive-result>>>");

        env.execute("指标计算服务");
    }

}
