package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortedListFieldAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = SortedListFieldAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("SORTEDLIMITLISTFIELD", aggregateFunctionAnnotation.name());
    }

    @Test
    void testCollective() {
        Collective collective = SortedListFieldAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(1, collective.retainStrategy());
        assertEquals(2, collective.keyStrategy());
    }

    @Test
    void testConstructor() {
        SortedListFieldAggregateFunction<Integer> sortedListFieldAggregateFunction = new SortedListFieldAggregateFunction<>();
        assertEquals(Integer.valueOf(10), sortedListFieldAggregateFunction.getLimit());
    }

}