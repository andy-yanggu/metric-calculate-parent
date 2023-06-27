package com.yanggu.metric_calculate.flink;


import com.esotericsoftware.kryo.Serializer;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.kryo.serializer.cube.MetricCubeSerializer;
import com.yanggu.metric_calculate.core2.kryo.serializer.window.*;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.window.*;
import com.yanggu.metric_calculate.flink.operator.NoKeyProcessTimeMiniBatchOperator;
import com.yanggu.metric_calculate.flink.pojo.DeriveCalculateData;
import com.yanggu.metric_calculate.flink.pojo.DeriveConfigData;
import com.yanggu.metric_calculate.flink.process_function.BatchReadProcessFunction;
import com.yanggu.metric_calculate.flink.process_function.DataTableProcessFunction;
import com.yanggu.metric_calculate.flink.process_function.KeyedDeriveBroadcastProcessFunction;
import com.yanggu.metric_calculate.flink.process_function.MetricDataMetricConfigBroadcastProcessFunction;
import com.yanggu.metric_calculate.flink.sink_function.BatchWriteSinkFunction;
import com.yanggu.metric_calculate.flink.source_function.TableDataSourceFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;

import java.util.List;

import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE;
import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE_CONFIG;

/**
 * 指标计算flink程序
 */
public class MetricCalculateFlinkJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.addDefaultKryoSerializer(MetricCube.class, new MetricCubeSerializer<>());
        env.addDefaultKryoSerializer(TumblingTimeWindow.class, new TumblingTimeWindowSerializer<>());
        env.addDefaultKryoSerializer(GlobalWindow.class, new GlobalWindowSerializer<>());
        env.addDefaultKryoSerializer(SlidingTimeWindow.class, new SlidingTimeWindowSerializer<>());
        env.addDefaultKryoSerializer(SlidingCountWindow.class, new SlidingCountWindowSerializer<>());
        env.addDefaultKryoSerializer(StatusWindow.class, new StatusWindowSerializer<>());
        env.addDefaultKryoSerializer(PatternWindow.class, new PatternWindowSerializer<>());

        //数据明细宽表配置流
        DataStreamSource<DataDetailsWideTable> tableSourceDataStream = env.addSource(new TableDataSourceFunction(), "Table-Source");

        //分流出派生指标配置流和全局指标配置流
        SingleOutputStreamOperator<Void> tableConfigDataStream = tableSourceDataStream.process(new DataTableProcessFunction());

        //派生指标配置数据流
        BroadcastStream<DeriveConfigData> deriveBroadcastStream = tableConfigDataStream
                .getSideOutput(new OutputTag<>(DERIVE_CONFIG, TypeInformation.of(DeriveConfigData.class)))
                .broadcast(new MapStateDescriptor<>("deriveMapState", Long.class, DeriveConfigData.class));

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
        SingleOutputStreamOperator<MetricCube> deriveBatchReadOperator = dataStream
                //分流出派生指标数据
                .getSideOutput(new OutputTag<>(DERIVE, deriveCalculateDataTypeInformation))
                //攒批读组件
                .transform("Derive Batch Read Operator", TypeInformation.of(new TypeHint<List<DeriveCalculateData>>() {}), deriveNoKeyProcessTimeMiniBatchOperator)
                //批读
                .process(batchReadProcessFunction)
                //根据维度进行keyBy
                .keyBy(DeriveCalculateData::getDimensionSet)
                .connect(deriveBroadcastStream)
                .process(new KeyedDeriveBroadcastProcessFunction());

        deriveBatchReadOperator
                .getSideOutput(new OutputTag<>("derive-result", TypeInformation.of(DeriveMetricCalculateResult.class)))
                .print("derive-result>>>");

        deriveBatchReadOperator
                //攒批写组件
                .transform("Derive Batch Update Operator", TypeInformation.of(new TypeHint<List<MetricCube>>() {}), deriveNoKeyProcessTimeMiniBatchOperator2)
                //批写
                .addSink(new BatchWriteSinkFunction());

        //全局指标数据流
        //SideOutputDataStream<JSONObject> globalDataStream = dataStream.getSideOutput(new OutputTag<>("global"));

        env.execute("指标计算服务");
    }

}
