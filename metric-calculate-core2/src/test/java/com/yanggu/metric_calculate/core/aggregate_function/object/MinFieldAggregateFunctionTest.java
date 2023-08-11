package com.yanggu.metric_calculate.core.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最小字段单元测试类
 */
class MinFieldAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = MinFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("MINFIELD", mergeType.value());
    }

    @Test
    void testObjective() {
        Objective objective = MinFieldAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(1, objective.retainStrategy());
        assertEquals(3, objective.keyStrategy());
    }

    @Test
    void testConstructor() {
        MinFieldAggregateFunction<Integer> minFieldAggregateFunction = new MinFieldAggregateFunction<>();
        assertNotNull(minFieldAggregateFunction);
    }

    @Test
    void createAccumulator() {
        MinFieldAggregateFunction<Integer> minFieldAggregateFunction = new MinFieldAggregateFunction<>();
        MutableObj<Integer> accumulator = minFieldAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        MinFieldAggregateFunction<Integer> minFieldAggregateFunction = new MinFieldAggregateFunction<>();
        MutableObj<Integer> accumulator = minFieldAggregateFunction.createAccumulator();

        minFieldAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        minFieldAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

    }

    @Test
    void getResult() {
        MinFieldAggregateFunction<Integer> minFieldAggregateFunction = new MinFieldAggregateFunction<>();
        MutableObj<Integer> accumulator = minFieldAggregateFunction.createAccumulator();

        minFieldAggregateFunction.add(1, accumulator);
        Integer result = minFieldAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void merge() {
        MinFieldAggregateFunction<Integer> minFieldAggregateFunction = new MinFieldAggregateFunction<>();
        MutableObj<Integer> accumulator1 = minFieldAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = minFieldAggregateFunction.createAccumulator();

        minFieldAggregateFunction.add(1, accumulator1);
        minFieldAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = minFieldAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(1), merge.get());
    }

}