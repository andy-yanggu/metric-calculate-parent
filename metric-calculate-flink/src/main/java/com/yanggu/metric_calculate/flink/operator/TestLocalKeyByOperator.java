package com.yanggu.metric_calculate.flink.operator;


import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.io.Serial;

public class TestLocalKeyByOperator {

    public static void main(String[] args) throws Exception {
        //启动一个webUI，指定本地WEB-UI端口号
        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, 8081);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        env.setParallelism(1);
        //开启检查点
        env.enableCheckpointing(5000L);

        KeySelector<String, String> keySelector = temp -> temp;
        SumAggregateFunction sumAggregateFunction = new SumAggregateFunction();
        TypeInformation<Tuple2<String, Long>> elementTypeInfo = Types.TUPLE(Types.STRING, Types.LONG);
        LocalKeyByAccumulateBatchOperator<String, String, Long, Long> operator =
                new LocalKeyByAccumulateBatchOperator<>(elementTypeInfo, keySelector, sumAggregateFunction);
        env.socketTextStream("localhost", 6666)
                .flatMap((String value, Collector<String> out) -> {
                    if ("test".equals(value)) {
                        throw new RuntimeException("测试重启作业");
                    }
                    String[] split = value.split(" ");
                    for (String string : split) {
                        out.collect(string);
                    }
                }).returns(Types.STRING)
                .transform("localKeyByOperator", elementTypeInfo, operator)
                .keyBy(tuple2 -> tuple2.f0)
                .reduce((tuple1, tuple2) -> Tuple2.of(tuple1.f0, tuple1.f1 + tuple2.f1))
                .map(tuple -> tuple.f0 + "->" + tuple.f1)
                .print("test>>>>>");

        env.execute();
    }

}

class SumAggregateFunction implements AggregateFunction<String, Long, Long> {

    @Serial
    private static final long serialVersionUID = -7955397394621268793L;

    @Override
    public Long createAccumulator() {
        return 0L;
    }

    @Override
    public Long add(String value, Long accumulator) {
        return ++accumulator;
    }

    @Override
    public Long getResult(Long accumulator) {
        return accumulator;
    }

    @Override
    public Long merge(Long a, Long b) {
        return a + b;
    }

}