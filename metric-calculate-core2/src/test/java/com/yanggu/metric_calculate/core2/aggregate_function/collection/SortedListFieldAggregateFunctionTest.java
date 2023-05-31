package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SortedListFieldAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = SortedListFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("SORTEDLIMITLISTFIELD", mergeType.value());
    }

    @Test
    public void testCollective() {
        Collective collective = SortedListFieldAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(1, collective.retainStrategy());
        assertEquals(2, collective.keyStrategy());
    }

    @Test
    public void testConstructor() {
        SortedListFieldAggregateFunction<Integer> sortedListFieldAggregateFunction = new SortedListFieldAggregateFunction<>();
        assertEquals(new Integer(10), sortedListFieldAggregateFunction.getLimit());
    }

}