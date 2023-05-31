package com.yanggu.metric_calculate.core2.aggregate_function.collection;


import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import org.junit.Test;

import static org.junit.Assert.*;

public class DistinctListFieldAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = DistinctListFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("DISTINCTLISTFIELD", mergeType.value());
    }

    @Test
    public void testCollective() {
        Collective collective = DistinctListFieldAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(1, collective.retainStrategy());
        assertEquals(1, collective.keyStrategy());
    }

    @Test
    public void testConstructor() {
        DistinctListFieldAggregateFunction<Object> distinctListFieldAggregateFunction = new DistinctListFieldAggregateFunction<>();
        assertNotNull(distinctListFieldAggregateFunction);
    }

}
