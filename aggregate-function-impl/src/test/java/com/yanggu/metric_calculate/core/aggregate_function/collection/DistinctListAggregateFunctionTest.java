package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

class DistinctListAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(DistinctListAggregateFunction.class, "DISTINCTLIST");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(DistinctListAggregateFunction.class, 1, 0);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(DistinctListAggregateFunction.class);
    }

}
