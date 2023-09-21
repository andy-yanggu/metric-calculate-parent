package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DistinctListAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = DistinctListAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("DISTINCTLIST", aggregateFunctionAnnotation.name());
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
