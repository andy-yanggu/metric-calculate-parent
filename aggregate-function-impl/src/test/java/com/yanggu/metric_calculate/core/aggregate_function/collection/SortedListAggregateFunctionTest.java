package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortedListAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = SortedListAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("SORTEDLIMITLIST", aggregateFunctionAnnotation.name());
    }

    @Test
    void testCollective() {
        Collective collective = SortedListAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(0, collective.retainStrategy());
        assertEquals(2, collective.keyStrategy());
    }

    @Test
    void testConstructor() {
        SortedListAggregateFunction<Integer> sortedListAggregateFunction = new SortedListAggregateFunction<>();
        assertEquals(Integer.valueOf(10), sortedListAggregateFunction.getLimit());
    }

}