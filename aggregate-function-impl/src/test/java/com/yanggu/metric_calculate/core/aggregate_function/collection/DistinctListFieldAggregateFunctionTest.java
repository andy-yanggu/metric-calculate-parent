package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

class DistinctListFieldAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(DistinctListFieldAggregateFunction.class, "DISTINCTLISTFIELD");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(DistinctListFieldAggregateFunction.class, 1, 1);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(DistinctListFieldAggregateFunction.class);
    }

}
