package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 最小对象单元测试类
 */
public class MinObjectAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = MinObjectAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("MINOBJECT", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = MinObjectAggregateFunction.class.getAnnotation(Objective.class);
        assertTrue(objective.retainObject());
        assertTrue(objective.useCompareField());
    }

    @Test
    public void testConstructor() {
        MinObjectAggregateFunction<Integer> minObjectAggregateFunction = new MinObjectAggregateFunction<>();
        assertNotNull(minObjectAggregateFunction);
    }

    @Test
    public void createAccumulator() {
        MinObjectAggregateFunction<Integer> minObjectAggregateFunction = new MinObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = minObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    public void add() {
        MinObjectAggregateFunction<Integer> minObjectAggregateFunction = new MinObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = minObjectAggregateFunction.createAccumulator();

        minObjectAggregateFunction.add(1, accumulator);
        assertEquals(new Integer(1), accumulator.get());

        minObjectAggregateFunction.add(2, accumulator);
        assertEquals(new Integer(1), accumulator.get());

    }

    @Test
    public void getResult() {
        MinObjectAggregateFunction<Integer> minObjectAggregateFunction = new MinObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = minObjectAggregateFunction.createAccumulator();

        minObjectAggregateFunction.add(1, accumulator);
        Integer result = minObjectAggregateFunction.getResult(accumulator);

        assertEquals(new Integer(1), result);
    }

    @Test
    public void merge() {
        MinObjectAggregateFunction<Integer> minObjectAggregateFunction = new MinObjectAggregateFunction<>();
        MutableObj<Integer> accumulator1 = minObjectAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = minObjectAggregateFunction.createAccumulator();

        minObjectAggregateFunction.add(1, accumulator1);
        minObjectAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = minObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(new Integer(1), merge.get());
    }

}