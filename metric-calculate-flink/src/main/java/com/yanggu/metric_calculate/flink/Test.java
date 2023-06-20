package com.yanggu.metric_calculate.flink;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.Global;
import com.yanggu.metric_calculate.flink.process_function.MyBroadcastProcessFunction;
import com.yanggu.metric_calculate.flink.source_function.TableDataSourceFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.SideOutputDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.Date;
import java.util.List;

@Slf4j
public class Test {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("172.20.7.143:9092")
                .setGroupId("metric-calculate")
                .setTopics("metric-calculate")
                .build();

        MapStateDescriptor<Long, DataDetailsWideTable> dataDetailsWideTableMapStateDescriptor =
                new MapStateDescriptor<>("DataDetailsWideTable", Long.class, DataDetailsWideTable.class);

        BroadcastStream<DataDetailsWideTable> broadcast = env
                .addSource(new TableDataSourceFunction(), "Table-Source")
                .broadcast(dataDetailsWideTableMapStateDescriptor);

        SingleOutputStreamOperator<Object> process = env
                .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka-Source")
                .connect(broadcast)
                .process(new MyBroadcastProcessFunction());

        SideOutputDataStream<JSONObject> derive = process.getSideOutput(new OutputTag<JSONObject>("derive"));
        SideOutputDataStream<JSONObject> global = process.getSideOutput(new OutputTag<JSONObject>("global"));

        //derive.connect()

        env.execute("指标计算服务");
    }



}
