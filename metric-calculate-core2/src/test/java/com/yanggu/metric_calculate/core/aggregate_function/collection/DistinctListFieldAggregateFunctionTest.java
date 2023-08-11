package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistinctListFieldAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = DistinctListFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("DISTINCTLISTFIELD", mergeType.value());
    }

    @Test
    void testCollective() {
        Collective collective = DistinctListFieldAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(1, collective.retainStrategy());
        assertEquals(1, collective.keyStrategy());
    }

    @Test
    void testConstructor() {
        DistinctListFieldAggregateFunction<Object> distinctListFieldAggregateFunction = new DistinctListFieldAggregateFunction<>();
        assertNotNull(distinctListFieldAggregateFunction);
    }

}
