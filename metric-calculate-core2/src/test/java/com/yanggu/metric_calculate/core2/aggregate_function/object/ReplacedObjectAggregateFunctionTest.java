package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 取代对象单元测试类
 */
public class ReplacedObjectAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = ReplacedObjectAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("REPLACEDOBJECT", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = ReplacedObjectAggregateFunction.class.getAnnotation(Objective.class);
        assertTrue(objective.retainObject());
        assertFalse(objective.useCompareField());
    }

    @Test
    public void testConstructor() {
        ReplacedObjectAggregateFunction<String> replacedObjectAggregateFunction = new ReplacedObjectAggregateFunction<>();
        assertNotNull(replacedObjectAggregateFunction);
    }

    @Test
    public void createAccumulator() {
        ReplacedObjectAggregateFunction<String> replacedObjectAggregateFunction = new ReplacedObjectAggregateFunction<>();
        MutableObj<String> accumulator = replacedObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    public void add() {
        ReplacedObjectAggregateFunction<String> replacedObjectAggregateFunction = new ReplacedObjectAggregateFunction<>();
        MutableObj<String> accumulator = replacedObjectAggregateFunction.createAccumulator();

        replacedObjectAggregateFunction.add("test1", accumulator);
        assertEquals("test1", accumulator.get());

        replacedObjectAggregateFunction.add("test2", accumulator);
        assertEquals("test2", accumulator.get());

    }

    @Test
    public void getResult() {
        ReplacedObjectAggregateFunction<String> replacedObjectAggregateFunction = new ReplacedObjectAggregateFunction<>();
        MutableObj<String> accumulator = replacedObjectAggregateFunction.createAccumulator();

        replacedObjectAggregateFunction.add("test1", accumulator);
        String result = replacedObjectAggregateFunction.getResult(accumulator);

        assertEquals("test1", result);
    }

    @Test
    public void merge() {
        ReplacedObjectAggregateFunction<String> replacedObjectAggregateFunction = new ReplacedObjectAggregateFunction<>();
        MutableObj<String> accumulator1 = replacedObjectAggregateFunction.createAccumulator();
        MutableObj<String> accumulator2 = replacedObjectAggregateFunction.createAccumulator();

        replacedObjectAggregateFunction.add("test1", accumulator1);
        replacedObjectAggregateFunction.add("test2", accumulator2);

        MutableObj<String> merge = replacedObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals("test2", merge.get());
    }

}