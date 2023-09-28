package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最小字段单元测试类
 */
class MinFieldAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(MinFieldAggregateFunction.class, "MINFIELD");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(MinFieldAggregateFunction.class, 3, 1);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(MinFieldAggregateFunction.class);
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