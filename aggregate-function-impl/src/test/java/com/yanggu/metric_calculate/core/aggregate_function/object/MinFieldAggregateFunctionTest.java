package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 最小字段单元测试类
 */
class MinFieldAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(MinFieldAggregateFunction.class, "MINFIELD");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(MinFieldAggregateFunction.class, 3, 1);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(MinFieldAggregateFunction.class);
    }

    @Test
    void getResult() {
        MinFieldAggregateFunction<Integer> minFieldAggregateFunction = new MinFieldAggregateFunction<>();
        var accumulator = minFieldAggregateFunction.createAccumulator();
        accumulator.set(new KeyValue<>(null, 1));

        Integer result = minFieldAggregateFunction.getResult(accumulator);
        assertEquals(Integer.valueOf(1), result);
    }

}