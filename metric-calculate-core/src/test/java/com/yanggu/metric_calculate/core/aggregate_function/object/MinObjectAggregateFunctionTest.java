package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最小对象单元测试类
 */
class MinObjectAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = MinObjectAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("MINOBJECT", mergeType.value());
    }

    @Test
    void testObjective() {
        Objective objective = MinObjectAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(2, objective.retainStrategy());
        assertEquals(3, objective.keyStrategy());
    }

    @Test
    void testConstructor() {
        MinObjectAggregateFunction<Integer> minObjectAggregateFunction = new MinObjectAggregateFunction<>();
        assertNotNull(minObjectAggregateFunction);
    }

    @Test
    void createAccumulator() {
        MinObjectAggregateFunction<Integer> minObjectAggregateFunction = new MinObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = minObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        MinObjectAggregateFunction<Integer> minObjectAggregateFunction = new MinObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = minObjectAggregateFunction.createAccumulator();

        minObjectAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        minObjectAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

    }

    @Test
    void getResult() {
        MinObjectAggregateFunction<Integer> minObjectAggregateFunction = new MinObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = minObjectAggregateFunction.createAccumulator();

        minObjectAggregateFunction.add(1, accumulator);
        Integer result = minObjectAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void merge() {
        MinObjectAggregateFunction<Integer> minObjectAggregateFunction = new MinObjectAggregateFunction<>();
        MutableObj<Integer> accumulator1 = minObjectAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = minObjectAggregateFunction.createAccumulator();

        minObjectAggregateFunction.add(1, accumulator1);
        minObjectAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = minObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(1), merge.get());
    }

}