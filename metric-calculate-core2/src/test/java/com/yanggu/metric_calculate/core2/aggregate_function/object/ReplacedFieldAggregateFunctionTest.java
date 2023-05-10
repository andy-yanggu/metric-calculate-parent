package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 取代字段单元测试类
 */
public class ReplacedFieldAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = ReplacedFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("REPLACEDFIELD", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = ReplacedFieldAggregateFunction.class.getAnnotation(Objective.class);
        assertFalse(objective.retainObject());
        assertFalse(objective.useCompareField());
    }

    @Test
    public void testConstructor() {
        ReplacedFieldAggregateFunction<String> replacedFieldAggregateFunction = new ReplacedFieldAggregateFunction<>();
        assertNotNull(replacedFieldAggregateFunction);
    }

}