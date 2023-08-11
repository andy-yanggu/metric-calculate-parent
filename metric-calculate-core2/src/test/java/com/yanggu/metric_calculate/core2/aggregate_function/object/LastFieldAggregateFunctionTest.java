package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Objective;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 取代字段单元测试类
 */
class LastFieldAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = LastFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("LASTFIELD", mergeType.value());
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