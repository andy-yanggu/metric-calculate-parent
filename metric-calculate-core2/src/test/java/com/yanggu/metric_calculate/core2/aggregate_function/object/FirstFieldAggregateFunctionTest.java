package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 占位字段单元测试类
 */
public class FirstFieldAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = FirstFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("FIRSTFIELD", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = FirstFieldAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(1, objective.retainStrategy());
        assertFalse(objective.useCompareField());
    }

    @Test
    public void testConstructor() {
        FirstFieldAggregateFunction<String> occupiedFieldAggregateFunction = new FirstFieldAggregateFunction<>();
        assertNotNull(occupiedFieldAggregateFunction);
    }

}