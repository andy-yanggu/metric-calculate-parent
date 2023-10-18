package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 最大字段单元测试类
 */
class MaxFieldAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(MaxFieldAggregateFunction.class, "MAXFIELD");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(MaxFieldAggregateFunction.class, 3, 1);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(MaxFieldAggregateFunction.class);
    }

    @Test
    void getResult() {
        MaxFieldAggregateFunction<Integer> maxFieldAggregateFunction = new MaxFieldAggregateFunction<>();
        var accumulator = maxFieldAggregateFunction.createAccumulator();
        accumulator.set(new Pair<>(null, 1));
        Integer result = maxFieldAggregateFunction.getResult(accumulator);
        assertEquals(Integer.valueOf(1), result);
    }

}