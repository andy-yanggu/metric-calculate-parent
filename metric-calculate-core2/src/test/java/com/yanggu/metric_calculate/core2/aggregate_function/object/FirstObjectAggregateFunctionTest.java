package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 占位对象单元测试类
 */
public class FirstObjectAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = FirstObjectAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("FIRSTOBJECT", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = FirstObjectAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(2, objective.retainStrategy());
        assertEquals(0, objective.keyStrategy());
    }

    @Test
    public void testConstructor() {
        FirstObjectAggregateFunction<String> firstObjectAggregateFunction = new FirstObjectAggregateFunction<>();
        assertNotNull(firstObjectAggregateFunction);
    }

    @Test
    public void createAccumulator() {
        FirstObjectAggregateFunction<String> firstObjectAggregateFunction = new FirstObjectAggregateFunction<>();
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    public void add() {
        FirstObjectAggregateFunction<String> firstObjectAggregateFunction = new FirstObjectAggregateFunction<>();
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();

        firstObjectAggregateFunction.add("test1", accumulator);
        assertEquals("test1", accumulator.get());

        firstObjectAggregateFunction.add("test2", accumulator);
        assertEquals("test1", accumulator.get());

    }

    @Test
    public void getResult() {
        FirstObjectAggregateFunction<String> firstObjectAggregateFunction = new FirstObjectAggregateFunction<>();
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();

        firstObjectAggregateFunction.add("test1", accumulator);
        String result = firstObjectAggregateFunction.getResult(accumulator);

        assertEquals("test1", result);
    }

    @Test
    public void merge() {
        FirstObjectAggregateFunction<String> firstObjectAggregateFunction = new FirstObjectAggregateFunction<>();
        MutableObj<String> accumulator1 = firstObjectAggregateFunction.createAccumulator();
        MutableObj<String> accumulator2 = firstObjectAggregateFunction.createAccumulator();

        firstObjectAggregateFunction.add("test1", accumulator1);
        firstObjectAggregateFunction.add("test2", accumulator2);

        MutableObj<String> merge = firstObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals("test1", merge.get());
    }

}