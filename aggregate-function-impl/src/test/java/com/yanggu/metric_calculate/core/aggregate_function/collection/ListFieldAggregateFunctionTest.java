package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ListFieldAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = ListFieldAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("LISTFIELD", aggregateFunctionAnnotation.name());
    }

    @Test
    void testCollective() {
        Collective collective = ListFieldAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(1, collective.retainStrategy());
        assertEquals(0, collective.keyStrategy());
    }

    @Test
    void testConstructor() {
        ListFieldAggregateFunction<Integer> listFieldAggregateFunction = new ListFieldAggregateFunction<>();
        assertEquals(Integer.valueOf(10), listFieldAggregateFunction.getLimit());
    }

}
