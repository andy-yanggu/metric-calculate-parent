package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最小值单元测试类
 */
class MinValueAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(MinValueAggregateFunction.class, "MINVALUE");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(MinValueAggregateFunction.class, 3, 0);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(MinValueAggregateFunction.class);
    }

    @Test
    void createAccumulator() {
        MinValueAggregateFunction<Integer> minValueAggregateFunction = new MinValueAggregateFunction<>();
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        MinValueAggregateFunction<Integer> minValueAggregateFunction = new MinValueAggregateFunction<>();
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        minValueAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

    }

    @Test
    void getResult() {
        MinValueAggregateFunction<Integer> minValueAggregateFunction = new MinValueAggregateFunction<>();
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator);
        Integer result = minValueAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void merge() {
        MinValueAggregateFunction<Integer> minValueAggregateFunction = new MinValueAggregateFunction<>();
        MutableObj<Integer> accumulator1 = minValueAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator1);
        minValueAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = minValueAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(1), merge.get());
    }

}