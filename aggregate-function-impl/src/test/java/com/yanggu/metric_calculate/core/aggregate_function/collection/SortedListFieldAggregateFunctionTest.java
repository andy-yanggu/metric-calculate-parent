package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortedListFieldAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = SortedListFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("SORTEDLIMITLISTFIELD", mergeType.value());
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