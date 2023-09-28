package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最大对象单元测试类
 */
class MaxObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(MaxObjectAggregateFunction.class, "MAXOBJECT");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(MaxObjectAggregateFunction.class, 3, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(MaxObjectAggregateFunction.class);
    }

    @Test
    void createAccumulator() {
        MaxObjectAggregateFunction<Integer> maxObjectAggregateFunction = new MaxObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        MaxObjectAggregateFunction<Integer> maxObjectAggregateFunction = new MaxObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        maxObjectAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(2), accumulator.get());

    }

    @Test
    void getResult() {
        MaxObjectAggregateFunction<Integer> maxObjectAggregateFunction = new MaxObjectAggregateFunction<>();
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator);
        Integer result = maxObjectAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void merge() {
        MaxObjectAggregateFunction<Integer> maxObjectAggregateFunction = new MaxObjectAggregateFunction<>();
        MutableObj<Integer> accumulator1 = maxObjectAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator1);
        maxObjectAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = maxObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(2), merge.get());
    }

}