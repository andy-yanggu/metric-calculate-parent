package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 最大值单元测试类
 */
public class MaxValueAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = MaxValueAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("MAXVALUE", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = MaxValueAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(0, objective.retainStrategy());
        assertEquals(3, objective.keyStrategy());
    }

    @Test
    public void testConstructor() {
        MaxValueAggregateFunction<Integer> maxValueAggregateFunction = new MaxValueAggregateFunction<>();
        assertNotNull(maxValueAggregateFunction);
    }

    @Test
    public void createAccumulator() {
        MaxValueAggregateFunction<Integer> maxValueAggregateFunction = new MaxValueAggregateFunction<>();
        MutableObj<Integer> accumulator = maxValueAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    public void add() {
        MaxValueAggregateFunction<Integer> maxValueAggregateFunction = new MaxValueAggregateFunction<>();
        MutableObj<Integer> accumulator = maxValueAggregateFunction.createAccumulator();

        maxValueAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        maxValueAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(2), accumulator.get());

    }

    @Test
    public void getResult() {
        MaxValueAggregateFunction<Integer> maxValueAggregateFunction = new MaxValueAggregateFunction<>();
        MutableObj<Integer> accumulator = maxValueAggregateFunction.createAccumulator();

        maxValueAggregateFunction.add(1, accumulator);
        Integer result = maxValueAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void merge() {
        MaxValueAggregateFunction<Integer> maxValueAggregateFunction = new MaxValueAggregateFunction<>();
        MutableObj<Integer> accumulator1 = maxValueAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = maxValueAggregateFunction.createAccumulator();

        maxValueAggregateFunction.add(1, accumulator1);
        maxValueAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = maxValueAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(2), merge.get());
    }

}