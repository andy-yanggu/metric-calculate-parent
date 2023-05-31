package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class LagFieldAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = LagFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("LAGFIELD", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = LagFieldAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(1, objective.retainStrategy());
        assertEquals(0, objective.keyStrategy());
    }

    @Test
    public void testConstructor() {
        LagFieldAggregateFunction<String> lagFieldAggregateFunction = new LagFieldAggregateFunction<>();
        assertEquals(1, lagFieldAggregateFunction.getOffset());
        assertNull(lagFieldAggregateFunction.getDefaultValue());
    }

    @Test
    public void createAccumulator() {
        LagFieldAggregateFunction<String> lagFieldAggregateFunction = new LagFieldAggregateFunction<>();
        LinkedList<String> accumulator = lagFieldAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
    }

    @Test
    public void add() {
        LagFieldAggregateFunction<String> lagFieldAggregateFunction = new LagFieldAggregateFunction<>();
        LinkedList<String> accumulator = lagFieldAggregateFunction.createAccumulator();
        lagFieldAggregateFunction.add("test1", accumulator);
        assertEquals(1, accumulator.size());
        assertEquals("test1", accumulator.get(0));

        lagFieldAggregateFunction.add("test2", accumulator);
        assertEquals(2, accumulator.size());
        assertEquals("test1", accumulator.get(0));
        assertEquals("test2", accumulator.get(1));

        lagFieldAggregateFunction.setOffset(2);
        lagFieldAggregateFunction.add("test3", accumulator);
        assertEquals(3, accumulator.size());
        assertEquals("test1", accumulator.get(0));
        assertEquals("test2", accumulator.get(1));
        assertEquals("test3", accumulator.get(2));

        lagFieldAggregateFunction.add("test4", accumulator);
        assertEquals(3, accumulator.size());
        assertEquals("test2", accumulator.get(0));
        assertEquals("test3", accumulator.get(1));
        assertEquals("test4", accumulator.get(2));
    }

    @Test
    public void getResult() {
        LagFieldAggregateFunction<String> lagFieldAggregateFunction = new LagFieldAggregateFunction<>();
        lagFieldAggregateFunction.setDefaultValue("defaultValue");

        LinkedList<String> accumulator = lagFieldAggregateFunction.createAccumulator();

        lagFieldAggregateFunction.add("test1", accumulator);
        String result = lagFieldAggregateFunction.getResult(accumulator);
        assertEquals("defaultValue", result);

        lagFieldAggregateFunction.add("test2", accumulator);
        result = lagFieldAggregateFunction.getResult(accumulator);
        assertEquals("test1", result);

        lagFieldAggregateFunction.setOffset(2);
        accumulator = lagFieldAggregateFunction.createAccumulator();

        lagFieldAggregateFunction.add("test3", accumulator);
        result = lagFieldAggregateFunction.getResult(accumulator);
        assertEquals("defaultValue", result);

        lagFieldAggregateFunction.add("test4", accumulator);
        result = lagFieldAggregateFunction.getResult(accumulator);
        assertEquals("defaultValue", result);

        lagFieldAggregateFunction.add("test5", accumulator);
        result = lagFieldAggregateFunction.getResult(accumulator);
        assertEquals("test3", result);

        lagFieldAggregateFunction.add("test6", accumulator);
        result = lagFieldAggregateFunction.getResult(accumulator);
        assertEquals("test4", result);

        //重新设置offset
        lagFieldAggregateFunction.setOffset(1);
        result = lagFieldAggregateFunction.getResult(accumulator);
        assertEquals("test5", result);
    }

    @Test
    public void merge() {
        LagFieldAggregateFunction<String> lagFieldAggregateFunction = new LagFieldAggregateFunction<>();
        lagFieldAggregateFunction.setOffset(4);

        LinkedList<String> accumulator1 = lagFieldAggregateFunction.createAccumulator();
        accumulator1.add("test1");
        accumulator1.add("test2");
        accumulator1.add("test3");
        accumulator1.add("test4");

        LinkedList<String> accumulator2 = lagFieldAggregateFunction.createAccumulator();
        accumulator2.add("test5");
        accumulator2.add("test6");

        LinkedList<String> merge = lagFieldAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(CollUtil.newLinkedList("test2", "test3", "test4", "test5", "test6"), merge);
    }

}