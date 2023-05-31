package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SortedListAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = SortedListAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("SORTEDLIMITLIST", mergeType.value());
    }

    @Test
    public void testCollective() {
        Collective collective = SortedListAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(0, collective.retainStrategy());
        assertTrue(collective.useSortedField());
        assertFalse(collective.useDistinctField());
    }

    @Test
    public void testConstructor() {
        SortedListAggregateFunction<Integer> sortedListAggregateFunction = new SortedListAggregateFunction<>();
        assertEquals(new Integer(10), sortedListAggregateFunction.getLimit());
    }

}