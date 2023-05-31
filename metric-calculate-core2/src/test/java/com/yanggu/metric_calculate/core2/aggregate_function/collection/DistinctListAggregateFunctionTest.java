package com.yanggu.metric_calculate.core2.aggregate_function.collection;


import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import org.junit.Test;

import static org.junit.Assert.*;

public class DistinctListAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = DistinctListAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("DISTINCTLIST", mergeType.value());
    }

    @Test
    public void testCollective() {
        Collective collective = DistinctListAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(0, collective.retainStrategy());
        assertFalse(collective.useSortedField());
        assertTrue(collective.useDistinctField());
    }

    @Test
    public void testConstructor() {
        DistinctListAggregateFunction<Object> distinctListAggregateFunction = new DistinctListAggregateFunction<>();
        assertNotNull(distinctListAggregateFunction);
    }

}
