package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

/**
 * 最小值单元测试类
 */
class MinValueAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(MinValueAggregateFunction.class, "MINVALUE");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(MinValueAggregateFunction.class, 3, 0);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(MinValueAggregateFunction.class);
    }

}