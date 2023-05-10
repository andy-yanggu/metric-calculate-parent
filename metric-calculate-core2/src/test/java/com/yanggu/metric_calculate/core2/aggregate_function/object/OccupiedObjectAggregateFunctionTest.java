package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 占位对象单元测试类
 */
public class OccupiedObjectAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = OccupiedObjectAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("OCCUPIEDOBJECT", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = OccupiedObjectAggregateFunction.class.getAnnotation(Objective.class);
        assertTrue(objective.retainObject());
        assertFalse(objective.useCompareField());
    }

    @Test
    public void testConstructor() {
        OccupiedObjectAggregateFunction<String> occupiedObjectAggregateFunction = new OccupiedObjectAggregateFunction<>();
        assertNotNull(occupiedObjectAggregateFunction);
    }

    @Test
    public void createAccumulator() {
        OccupiedObjectAggregateFunction<String> occupiedObjectAggregateFunction = new OccupiedObjectAggregateFunction<>();
        MutableObj<String> accumulator = occupiedObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    public void add() {
        OccupiedObjectAggregateFunction<String> occupiedObjectAggregateFunction = new OccupiedObjectAggregateFunction<>();
        MutableObj<String> accumulator = occupiedObjectAggregateFunction.createAccumulator();

        occupiedObjectAggregateFunction.add("test1", accumulator);
        assertEquals("test1", accumulator.get());

        occupiedObjectAggregateFunction.add("test2", accumulator);
        assertEquals("test1", accumulator.get());

    }

    @Test
    public void getResult() {
        OccupiedObjectAggregateFunction<String> occupiedObjectAggregateFunction = new OccupiedObjectAggregateFunction<>();
        MutableObj<String> accumulator = occupiedObjectAggregateFunction.createAccumulator();

        occupiedObjectAggregateFunction.add("test1", accumulator);
        String result = occupiedObjectAggregateFunction.getResult(accumulator);

        assertEquals("test1", result);
    }

    @Test
    public void merge() {
        OccupiedObjectAggregateFunction<String> occupiedObjectAggregateFunction = new OccupiedObjectAggregateFunction<>();
        MutableObj<String> accumulator1 = occupiedObjectAggregateFunction.createAccumulator();
        MutableObj<String> accumulator2 = occupiedObjectAggregateFunction.createAccumulator();

        occupiedObjectAggregateFunction.add("test1", accumulator1);
        occupiedObjectAggregateFunction.add("test2", accumulator2);

        MutableObj<String> merge = occupiedObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals("test1", merge.get());
    }

}