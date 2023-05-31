package com.yanggu.metric_calculate.core2.aggregate_function.collection;


import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ListFieldAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = ListFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("LISTFIELD", mergeType.value());
    }

    @Test
    public void testCollective() {
        Collective collective = ListFieldAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(1, collective.retainStrategy());
        assertFalse(collective.useSortedField());
        assertFalse(collective.useDistinctField());
    }

    @Test
    public void testConstructor() {
        ListFieldAggregateFunction<Integer> listFieldAggregateFunction = new ListFieldAggregateFunction<>();
        assertEquals(new Integer(10), listFieldAggregateFunction.getLimit());
    }

}
