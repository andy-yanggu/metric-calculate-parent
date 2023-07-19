package com.yanggu.metric_calculate.core2.aggregate_function.collection;


import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DistinctListAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = DistinctListAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("DISTINCTLIST", mergeType.value());
    }

    @Test
    void testCollective() {
        Collective collective = DistinctListAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(0, collective.retainStrategy());
        assertEquals(1, collective.keyStrategy());
    }

    @Test
    void testConstructor() {
        DistinctListAggregateFunction<Object> distinctListAggregateFunction = new DistinctListAggregateFunction<>();
        assertNotNull(distinctListAggregateFunction);
    }

}
