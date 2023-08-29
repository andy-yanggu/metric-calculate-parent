package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 占位字段单元测试类
 */
class FirstFieldAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = FirstFieldAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("FIRSTFIELD", aggregateFunctionAnnotation.name());
    }

    @Test
    void testObjective() {
        Objective objective = FirstFieldAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(1, objective.retainStrategy());
        assertEquals(0, objective.keyStrategy());
    }

    @Test
    void testConstructor() {
        FirstFieldAggregateFunction<String> occupiedFieldAggregateFunction = new FirstFieldAggregateFunction<>();
        assertNotNull(occupiedFieldAggregateFunction);
    }

}