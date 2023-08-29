package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DistinctListFieldAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = DistinctListFieldAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("DISTINCTLISTFIELD", aggregateFunctionAnnotation.name());
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
