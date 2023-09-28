package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;


class ListFieldAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(ListFieldAggregateFunction.class, "LISTFIELD");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(ListFieldAggregateFunction.class, 0, 1);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(ListFieldAggregateFunction.class);
    }

}
