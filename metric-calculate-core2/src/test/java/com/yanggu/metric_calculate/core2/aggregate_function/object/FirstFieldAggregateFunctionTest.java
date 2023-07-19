package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Objective;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 占位字段单元测试类
 */
class FirstFieldAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = FirstFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("FIRSTFIELD", mergeType.value());
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