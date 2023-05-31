package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 最大对象单元测试类
 */
public class MaxObjectAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = MaxObjectAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("MAXOBJECT", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = MaxObjectAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(2, objective.retainStrategy());
        assertTrue(objective.useCompareField());
    }

    @Test
    public void testConstructor() {
        MaxObjectAggregateFunction<Integer> maxObjectAggregateFunction = new MaxObjectAggregateFunction<>();
        assertNotNull(maxObjectAggregateFunction);
    }

    @Test
    public void createAccumulator() {
        MaxObjectAggregateFunction<Integer> maxObjectAggregateFunction = new MaxObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    public void add() {
        MaxObjectAggregateFunction<Integer> maxObjectAggregateFunction = new MaxObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator);
        assertEquals(new Integer(1), accumulator.get());

        maxObjectAggregateFunction.add(2, accumulator);
        assertEquals(new Integer(2), accumulator.get());

    }

    @Test
    public void getResult() {
        MaxObjectAggregateFunction<Integer> maxObjectAggregateFunction = new MaxObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator);
        Integer result = maxObjectAggregateFunction.getResult(accumulator);

        assertEquals(new Integer(1), result);
    }

    @Test
    public void merge() {
        MaxObjectAggregateFunction<Integer> maxObjectAggregateFunction = new MaxObjectAggregateFunction<>();
        MutableObj<Integer> accumulator1 = maxObjectAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator1);
        maxObjectAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = maxObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(new Integer(2), merge.get());
    }

}