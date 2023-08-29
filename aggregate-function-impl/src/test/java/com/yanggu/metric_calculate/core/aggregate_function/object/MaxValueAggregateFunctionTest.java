package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最大值单元测试类
 */
class MaxValueAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = MaxValueAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("MAXVALUE", aggregateFunctionAnnotation.name());
    }

    @Test
    void testObjective() {
        Objective objective = MaxValueAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(0, objective.retainStrategy());
        assertEquals(3, objective.keyStrategy());
    }

    @Test
    void testConstructor() {
        MaxValueAggregateFunction<Integer> maxValueAggregateFunction = new MaxValueAggregateFunction<>();
        assertNotNull(maxValueAggregateFunction);
    }

    @Test
    void createAccumulator() {
        MaxValueAggregateFunction<Integer> maxValueAggregateFunction = new MaxValueAggregateFunction<>();
        MutableObj<Integer> accumulator = maxValueAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        MaxValueAggregateFunction<Integer> maxValueAggregateFunction = new MaxValueAggregateFunction<>();
        MutableObj<Integer> accumulator = maxValueAggregateFunction.createAccumulator();

        maxValueAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        maxValueAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(2), accumulator.get());

    }

    @Test
    void getResult() {
        MaxValueAggregateFunction<Integer> maxValueAggregateFunction = new MaxValueAggregateFunction<>();
        MutableObj<Integer> accumulator = maxValueAggregateFunction.createAccumulator();

        maxValueAggregateFunction.add(1, accumulator);
        Integer result = maxValueAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void merge() {
        MaxValueAggregateFunction<Integer> maxValueAggregateFunction = new MaxValueAggregateFunction<>();
        MutableObj<Integer> accumulator1 = maxValueAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = maxValueAggregateFunction.createAccumulator();

        maxValueAggregateFunction.add(1, accumulator1);
        maxValueAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = maxValueAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(2), merge.get());
    }

}