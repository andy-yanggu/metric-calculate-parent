package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 最小值单元测试类
 */
public class MinValueAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = MinValueAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("MINVALUE", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = MinValueAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(0, objective.retainStrategy());
        assertEquals(3, objective.keyStrategy());
    }

    @Test
    public void testConstructor() {
        MinValueAggregateFunction<Integer> minValueAggregateFunction = new MinValueAggregateFunction<>();
        assertNotNull(minValueAggregateFunction);
    }

    @Test
    public void createAccumulator() {
        MinValueAggregateFunction<Integer> minValueAggregateFunction = new MinValueAggregateFunction<>();
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    public void add() {
        MinValueAggregateFunction<Integer> minValueAggregateFunction = new MinValueAggregateFunction<>();
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        minValueAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

    }

    @Test
    public void getResult() {
        MinValueAggregateFunction<Integer> minValueAggregateFunction = new MinValueAggregateFunction<>();
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator);
        Integer result = minValueAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void merge() {
        MinValueAggregateFunction<Integer> minValueAggregateFunction = new MinValueAggregateFunction<>();
        MutableObj<Integer> accumulator1 = minValueAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator1);
        minValueAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = minValueAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(1), merge.get());
    }

}