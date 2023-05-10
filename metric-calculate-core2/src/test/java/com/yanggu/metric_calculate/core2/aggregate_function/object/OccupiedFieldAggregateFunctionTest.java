package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 占位字段单元测试类
 */
public class OccupiedFieldAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = OccupiedFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("OCCUPIEDFIELD", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = OccupiedFieldAggregateFunction.class.getAnnotation(Objective.class);
        assertFalse(objective.retainObject());
        assertFalse(objective.useCompareField());
    }

    @Test
    public void testConstructor() {
        OccupiedFieldAggregateFunction<String> occupiedFieldAggregateFunction = new OccupiedFieldAggregateFunction<>();
        assertNotNull(occupiedFieldAggregateFunction);
    }

}