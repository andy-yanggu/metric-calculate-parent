package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

class LagFieldAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(LagFieldAggregateFunction.class, "LAGFIELD");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(LagFieldAggregateFunction.class, 0, 1);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(LagFieldAggregateFunction.class);
    }

}