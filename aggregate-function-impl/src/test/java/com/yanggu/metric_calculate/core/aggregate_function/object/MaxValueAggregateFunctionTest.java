package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 最大值单元测试类
 */
class MaxValueAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(MaxValueAggregateFunction.class, "MAXVALUE");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(MaxValueAggregateFunction.class, 3, 0);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(MaxValueAggregateFunction.class);
    }

    @Test
    void getResult() {
        var maxValueAggregateFunction = new MaxValueAggregateFunction();
        var accumulator = maxValueAggregateFunction.createAccumulator();
        accumulator.set(new Pair<>(new MultiFieldData(List.of(1)), null));
        var result = maxValueAggregateFunction.getResult(accumulator);
        assertEquals(List.of(1), result);
    }

}