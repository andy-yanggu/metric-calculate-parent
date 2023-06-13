package com.yanggu.metric_calculate.flink;


import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Test {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("172.20.7.143:9092")
                .setGroupId("metric-calculate")
                .setTopics("metric-calculate")
                .build();

        //env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka-Source")
        //                .addSink()

        env.execute("指标计算服务");
    }

}
