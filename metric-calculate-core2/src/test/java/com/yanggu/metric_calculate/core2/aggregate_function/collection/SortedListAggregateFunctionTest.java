package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SortedListAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = SortedListAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("SORTEDLIMITLIST", mergeType.value());
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