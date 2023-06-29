package com.yanggu.metric_calculate.flink;


import cn.hutool.core.collection.BoundedPriorityQueue;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.lang.mutable.MutableObj;
import cn.hutool.core.lang.mutable.MutablePair;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrder;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core2.kryo.serializer.acc.*;
import com.yanggu.metric_calculate.core2.kryo.serializer.cube.DimensionSetSerializer;
import com.yanggu.metric_calculate.core2.kryo.serializer.cube.MetricCubeSerializer;
import com.yanggu.metric_calculate.core2.kryo.serializer.util.KryoCollectionSerializer;
import com.yanggu.metric_calculate.core2.kryo.serializer.util.KryoMapSerializer;
import com.yanggu.metric_calculate.core2.kryo.serializer.util.KryoTreeMapSerializer;
import com.yanggu.metric_calculate.core2.kryo.serializer.window.*;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.util.KeyValue;
import com.yanggu.metric_calculate.core2.window.*;
import com.yanggu.metric_calculate.flink.operator.NoKeyProcessTimeMiniBatchOperator;
import com.yanggu.metric_calculate.flink.pojo.DeriveCalculateData;
import com.yanggu.metric_calculate.flink.pojo.DeriveConfigData;
import com.yanggu.metric_calculate.flink.process_function.*;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        env.registerTypeWithKryoSerializer(MutablePair.class, new MutablePairSerializer<>());
        env.registerTypeWithKryoSerializer(BoundedPriorityQueue.class, new BoundedPriorityQueueSerializer<>());
        env.registerTypeWithKryoSerializer(MutableObj.class, new MutableObjectSerializer<>());
        env.registerTypeWithKryoSerializer(Pair.class, new PairSerializer<>());
        env.registerTypeWithKryoSerializer(MultiFieldDistinctKey.class, new MultiFieldDistinctKeySerializer());
        //env.registerTypeWithKryoSerializer(NodePattern.class, new NodePatternSerializer());
        env.registerTypeWithKryoSerializer(FieldOrder.class, new FieldOrderSerializer());
        env.registerTypeWithKryoSerializer(MultiFieldOrderCompareKey.class, new MultiFieldOrderCompareKeySerializer());
        env.registerTypeWithKryoSerializer(KeyValue.class, new KeyValueSerializer<>());
        env.registerTypeWithKryoSerializer(ArrayList.class, new KryoCollectionSerializer<ArrayList<Object>>());
        env.registerTypeWithKryoSerializer(TreeMap.class, new KryoTreeMapSerializer());
        env.registerTypeWithKryoSerializer(HashMap.class, new KryoMapSerializer<HashMap<Object, Object>>());

        //MetricCube序列化器和反序列化器
        env.registerTypeWithKryoSerializer(DimensionSet.class, new DimensionSetSerializer());
        env.registerTypeWithKryoSerializer(MetricCube.class, new MetricCubeSerializer<>());

        //数据明细宽表配置流
        DataStreamSource<DataDetailsWideTable> tableSourceDataStream = env.addSource(new TableDataSourceFunction(), "Table-Source");

        //分流出派生指标配置流和全局指标配置流
        SingleOutputStreamOperator<Void> tableConfigDataStream = tableSourceDataStream.process(new DataTableProcessFunction());

        //派生指标配置数据流
        BroadcastStream<DeriveConfigData> deriveBroadcastStream = tableConfigDataStream
                .getSideOutput(new OutputTag<>(DERIVE_CONFIG, TypeInformation.of(DeriveConfigData.class)))
                .broadcast(deriveMapStateDescriptor);

        //将数据明细宽表数据流进行广播
        BroadcastStream<DataDetailsWideTable> tableSourceBroadcast = tableSourceDataStream
                .broadcast(new MapStateDescriptor<>("DataDetailsWideTable", Long.class, DataDetailsWideTable.class));

        //KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
        //        .setBootstrapServers("172.20.7.143:9092")
        //        .setGroupId("metric-calculate")
        //        .setTopics("metric-calculate")
        //        .build();

        SingleOutputStreamOperator<Void> dataStream = env
                //.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka-Source")
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
                .transform("Derive Batch Read Operator", TypeInformation.of(new TypeHint<List<DeriveCalculateData>>() {}), deriveNoKeyProcessTimeMiniBatchOperator)
                //批读
                .process(batchReadProcessFunction)
                //根据维度进行keyBy
                .keyBy(DeriveCalculateData::getDimensionSet)
                //连接派生指标配置流
                .connect(deriveBroadcastStream)
                //计算派生指标
                .process(new KeyedDeriveBroadcastProcessFunction())
                //攒批写组件
                .transform("Derive Batch Update Operator", TypeInformation.of(new TypeHint<List<MetricCube>>() {}), deriveNoKeyProcessTimeMiniBatchOperator2)
                //批写
                .process(new BatchWriteProcessFunction())
                //连接派生指标配置流
                .connect(deriveBroadcastStream)
                //返回计算后的数据
                .process(new DeriveBroadcastProcessFunction())
                .print("derive-result>>>");

        //全局指标数据流
        //SideOutputDataStream<JSONObject> globalDataStream = dataStream.getSideOutput(new OutputTag<>("global"));

        env.execute("指标计算服务");
    }

}
