package com.yanggu.metric_calculate.flink;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class BatchStreamDemo {
    public static void main(String[] args) throws Exception {
        doBatch();
//        doStream();
    }

    public static void doBatch() throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        streamExecutionEnvironment.setRuntimeMode(RuntimeExecutionMode.BATCH);
        DataStreamSource<Integer> integerDataStreamSource = streamExecutionEnvironment.fromElements(1, 2, 3, 4, 5, 6);
        integerDataStreamSource.keyBy((value) -> 1).sum(0).print("batch>>>");
        streamExecutionEnvironment.execute();
    }

    public static void doStream() throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        streamExecutionEnvironment.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        DataStreamSource<Integer> integerDataStreamSource = streamExecutionEnvironment.fromElements(1, 2, 3, 4, 5, 6);
        integerDataStreamSource.keyBy((value) -> 1).sum(0).print();
        streamExecutionEnvironment.execute();
    }
}