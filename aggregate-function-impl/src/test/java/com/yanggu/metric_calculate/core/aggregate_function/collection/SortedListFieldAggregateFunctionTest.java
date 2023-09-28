package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

class SortedListFieldAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(SortedListFieldAggregateFunction.class, "SORTEDLIMITLISTFIELD");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(SortedListFieldAggregateFunction.class, 2, 1);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(SortedListFieldAggregateFunction.class);
    }

}