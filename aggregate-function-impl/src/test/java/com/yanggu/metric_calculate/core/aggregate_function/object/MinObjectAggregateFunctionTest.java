package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最小对象单元测试类
 */
class MinObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(MinObjectAggregateFunction.class, "MINOBJECT");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(MinObjectAggregateFunction.class, 3, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(MinObjectAggregateFunction.class);
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