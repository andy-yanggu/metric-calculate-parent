package com.yanggu.metric_calculate.flink;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class ExecutionModeDemo {

    public static void main(String[] args) throws Exception {
        //流模式下，输出中间结果
        doStream();
        //批模式下，不输出中间结果，只输出最终结果
        doBatch();
    }

    public static void doStream() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);

        env.fromData(1, 2, 3, 4, 5, 6)
                .keyBy(data -> 0)
                .sum(0)
                .print("stream>>>");

        env.execute();
    }

    public static void doBatch() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.BATCH);

        env.fromData(1, 2, 3, 4, 5, 6)
                .keyBy(data -> 0)
                .sum(0)
                .print("batch>>>");

        env.execute();
    }

}