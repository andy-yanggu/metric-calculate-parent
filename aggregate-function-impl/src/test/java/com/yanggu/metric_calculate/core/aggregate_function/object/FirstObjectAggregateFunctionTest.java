package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 占位对象单元测试类
 */
class FirstObjectAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = FirstObjectAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("FIRSTOBJECT", aggregateFunctionAnnotation.name());
    }

    @Test
    void testObjective() {
        Objective objective = FirstObjectAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(2, objective.retainStrategy());
        assertEquals(0, objective.keyStrategy());
    }

    @Test
    void testConstructor() {
        FirstObjectAggregateFunction<String> firstObjectAggregateFunction = new FirstObjectAggregateFunction<>();
        assertNotNull(firstObjectAggregateFunction);
    }

    @Test
    void createAccumulator() {
        FirstObjectAggregateFunction<String> firstObjectAggregateFunction = new FirstObjectAggregateFunction<>();
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        FirstObjectAggregateFunction<String> firstObjectAggregateFunction = new FirstObjectAggregateFunction<>();
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();

        firstObjectAggregateFunction.add("test1", accumulator);
        assertEquals("test1", accumulator.get());

        firstObjectAggregateFunction.add("test2", accumulator);
        assertEquals("test1", accumulator.get());

    }

    @Test
    void getResult() {
        FirstObjectAggregateFunction<String> firstObjectAggregateFunction = new FirstObjectAggregateFunction<>();
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();

        firstObjectAggregateFunction.add("test1", accumulator);
        String result = firstObjectAggregateFunction.getResult(accumulator);

        assertEquals("test1", result);
    }

    @Test
    void merge() {
        FirstObjectAggregateFunction<String> firstObjectAggregateFunction = new FirstObjectAggregateFunction<>();
        MutableObj<String> accumulator1 = firstObjectAggregateFunction.createAccumulator();
        MutableObj<String> accumulator2 = firstObjectAggregateFunction.createAccumulator();

        firstObjectAggregateFunction.add("test1", accumulator1);
        firstObjectAggregateFunction.add("test2", accumulator2);

        MutableObj<String> merge = firstObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals("test1", merge.get());
    }

}