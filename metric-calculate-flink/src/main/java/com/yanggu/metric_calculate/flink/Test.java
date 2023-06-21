package com.yanggu.metric_calculate.flink;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.Global;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import com.yanggu.metric_calculate.flink.process_function.KeyedBroadcastProcessFunction2;
import com.yanggu.metric_calculate.flink.process_function.MyBroadcastProcessFunction;
import com.yanggu.metric_calculate.flink.process_function.MyProcessFunction1;
import com.yanggu.metric_calculate.flink.process_function.ProcessFunction2;
import com.yanggu.metric_calculate.flink.source_function.TableDataSourceFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.runtime.state.JavaSerializer;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;

/**
 * 指标计算flink程序
 */
public class Test {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        MapStateDescriptor<Long, DataDetailsWideTable> dataDetailsWideTableMapStateDescriptor =
                new MapStateDescriptor<>("DataDetailsWideTable", Long.class, DataDetailsWideTable.class);

        DataStreamSource<DataDetailsWideTable> tableSource = env
                .addSource(new TableDataSourceFunction(), "Table-Source");

        tableSource.print("table source >>>");

        SingleOutputStreamOperator<Void> process1 = tableSource.process(new ProcessFunction2());

        //deriveConfigDataStream
        MapStateDescriptor<Long, JSONObject> deriveMapStateDescriptor =
                new MapStateDescriptor<>("deriveMapState", Long.class, JSONObject.class);

        BroadcastStream<JSONObject> deriveBroadcastStream = process1
                .getSideOutput(new OutputTag<JSONObject>("derive-config"))
                .broadcast(deriveMapStateDescriptor);

        BroadcastStream<DataDetailsWideTable> broadcast = tableSource.broadcast(dataDetailsWideTableMapStateDescriptor);

        //KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
        //        .setBootstrapServers("172.20.7.143:9092")
        //        .setGroupId("metric-calculate")
        //        .setTopics("metric-calculate")
        //        .build();

        SingleOutputStreamOperator<Void> dataStream = env
                //.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka-Source")
                .socketTextStream("localhost", 6666)
                .connect(broadcast)
                .process(new MyBroadcastProcessFunction());

        NoKeyProcessTimeMiniBatchOperator<JSONObject> deriveNoKeyProcessTimeMiniBatchOperator = new NoKeyProcessTimeMiniBatchOperator<>();
        deriveNoKeyProcessTimeMiniBatchOperator.setElementSerializer(new JavaSerializer<>());

        //派生指标数据流
        dataStream
                //分流出派生指标数据
                .getSideOutput(new OutputTag<JSONObject>("derive"))
                //攒批读
                .transform("Derive Batch Read Operator", TypeInformation.of(new TypeHint<List<JSONObject>>() {}), deriveNoKeyProcessTimeMiniBatchOperator)
                .process(new MyProcessFunction1())
                .keyBy(tempObj -> tempObj.get("dimensionSet", DimensionSet.class))
                .connect(deriveBroadcastStream)
                .process(new KeyedBroadcastProcessFunction2())
                .print("derive calculate result >>>");

        //全局指标数据流
        //SideOutputDataStream<JSONObject> globalDataStream = dataStream.getSideOutput(new OutputTag<>("global"));

        env.execute("指标计算服务");
    }



}
