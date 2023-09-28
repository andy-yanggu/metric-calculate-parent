package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最大字段单元测试类
 */
class MaxFieldAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(MaxFieldAggregateFunction.class, "MAXFIELD");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(MaxFieldAggregateFunction.class, 3, 1);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(MaxFieldAggregateFunction.class);
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