package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最大字段单元测试类
 */
class MaxFieldAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = MaxFieldAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("MAXFIELD", aggregateFunctionAnnotation.name());
    }

    @Test
    void testObjective() {
        Objective objective = MaxFieldAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(1, objective.retainStrategy());
        assertEquals(3, objective.keyStrategy());
    }

    @Test
    void testConstructor() {
        MaxFieldAggregateFunction<Integer> maxFieldAggregateFunction = new MaxFieldAggregateFunction<>();
        assertNotNull(maxFieldAggregateFunction);
    }

    @Test
    void createAccumulator() {
        MaxFieldAggregateFunction<Integer> maxFieldAggregateFunction = new MaxFieldAggregateFunction<>();
        MutableObj<Integer> accumulator = maxFieldAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        MaxFieldAggregateFunction<Integer> maxFieldAggregateFunction = new MaxFieldAggregateFunction<>();
        MutableObj<Integer> accumulator = maxFieldAggregateFunction.createAccumulator();

        maxFieldAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        maxFieldAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(2), accumulator.get());

    }

    @Test
    void getResult() {
        MaxFieldAggregateFunction<Integer> maxFieldAggregateFunction = new MaxFieldAggregateFunction<>();
        MutableObj<Integer> accumulator = maxFieldAggregateFunction.createAccumulator();

        maxFieldAggregateFunction.add(1, accumulator);
        Integer result = maxFieldAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void merge() {
        MaxFieldAggregateFunction<Integer> maxFieldAggregateFunction = new MaxFieldAggregateFunction<>();
        MutableObj<Integer> accumulator1 = maxFieldAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = maxFieldAggregateFunction.createAccumulator();

        maxFieldAggregateFunction.add(1, accumulator1);
        maxFieldAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = maxFieldAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(2), merge.get());
    }

}