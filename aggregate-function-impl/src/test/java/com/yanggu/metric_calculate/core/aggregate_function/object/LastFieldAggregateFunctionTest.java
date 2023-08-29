package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 取代字段单元测试类
 */
class LastFieldAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = LastFieldAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("LASTFIELD", aggregateFunctionAnnotation.name());
    }

    @Test
    void testObjective() {
        Objective objective = LastFieldAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(1, objective.retainStrategy());
        assertEquals(0, objective.keyStrategy());
    }

    @Test
    void testConstructor() {
        LastFieldAggregateFunction<String> replacedFieldAggregateFunction = new LastFieldAggregateFunction<>();
        assertNotNull(replacedFieldAggregateFunction);
    }

}