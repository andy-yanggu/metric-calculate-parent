package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 取代对象单元测试类
 */
public class LastObjectAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = LastObjectAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("LASTOBJECT", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = LastObjectAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(2, objective.retainStrategy());
        assertEquals(0, objective.keyStrategy());
    }

    @Test
    public void testConstructor() {
        LastObjectAggregateFunction<String> lastObjectAggregateFunction = new LastObjectAggregateFunction<>();
        assertNotNull(lastObjectAggregateFunction);
    }

    @Test
    public void createAccumulator() {
        LastObjectAggregateFunction<String> lastObjectAggregateFunction = new LastObjectAggregateFunction<>();
        MutableObj<String> accumulator = lastObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    public void add() {
        LastObjectAggregateFunction<String> lastObjectAggregateFunction = new LastObjectAggregateFunction<>();
        MutableObj<String> accumulator = lastObjectAggregateFunction.createAccumulator();

        lastObjectAggregateFunction.add("test1", accumulator);
        assertEquals("test1", accumulator.get());

        lastObjectAggregateFunction.add("test2", accumulator);
        assertEquals("test2", accumulator.get());

    }

    @Test
    public void getResult() {
        LastObjectAggregateFunction<String> lastObjectAggregateFunction = new LastObjectAggregateFunction<>();
        MutableObj<String> accumulator = lastObjectAggregateFunction.createAccumulator();

        lastObjectAggregateFunction.add("test1", accumulator);
        String result = lastObjectAggregateFunction.getResult(accumulator);

        assertEquals("test1", result);
    }

    @Test
    public void merge() {
        LastObjectAggregateFunction<String> lastObjectAggregateFunction = new LastObjectAggregateFunction<>();
        MutableObj<String> accumulator1 = lastObjectAggregateFunction.createAccumulator();
        MutableObj<String> accumulator2 = lastObjectAggregateFunction.createAccumulator();

        lastObjectAggregateFunction.add("test1", accumulator1);
        lastObjectAggregateFunction.add("test2", accumulator2);

        MutableObj<String> merge = lastObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals("test2", merge.get());
    }

}