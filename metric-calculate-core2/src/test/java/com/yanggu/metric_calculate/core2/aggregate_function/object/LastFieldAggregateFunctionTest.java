package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 取代字段单元测试类
 */
public class LastFieldAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = LastFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("LASTFIELD", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = LastFieldAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(1, objective.retainStrategy());
        assertFalse(objective.useCompareField());
    }

    @Test
    public void testConstructor() {
        LastFieldAggregateFunction<String> replacedFieldAggregateFunction = new LastFieldAggregateFunction<>();
        assertNotNull(replacedFieldAggregateFunction);
    }

}