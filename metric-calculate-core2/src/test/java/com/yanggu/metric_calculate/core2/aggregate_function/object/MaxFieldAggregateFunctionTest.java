package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 最大字段单元测试类
 */
public class MaxFieldAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = MaxFieldAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("MAXFIELD", mergeType.value());
    }

    @Test
    public void testObjective() {
        Objective objective = MaxFieldAggregateFunction.class.getAnnotation(Objective.class);
        assertEquals(1, objective.retainStrategy());
        assertEquals(3, objective.keyStrategy());
    }

    @Test
    public void testConstructor() {
        MaxFieldAggregateFunction<Integer> maxFieldAggregateFunction = new MaxFieldAggregateFunction<>();
        assertNotNull(maxFieldAggregateFunction);
    }

    @Test
    public void createAccumulator() {
        MaxFieldAggregateFunction<Integer> maxFieldAggregateFunction = new MaxFieldAggregateFunction<>();
        MutableObj<Integer> accumulator = maxFieldAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    public void add() {
        MaxFieldAggregateFunction<Integer> maxFieldAggregateFunction = new MaxFieldAggregateFunction<>();
        MutableObj<Integer> accumulator = maxFieldAggregateFunction.createAccumulator();

        maxFieldAggregateFunction.add(1, accumulator);
        assertEquals(new Integer(1), accumulator.get());

        maxFieldAggregateFunction.add(2, accumulator);
        assertEquals(new Integer(2), accumulator.get());

    }

    @Test
    public void getResult() {
        MaxFieldAggregateFunction<Integer> maxFieldAggregateFunction = new MaxFieldAggregateFunction<>();
        MutableObj<Integer> accumulator = maxFieldAggregateFunction.createAccumulator();

        maxFieldAggregateFunction.add(1, accumulator);
        Integer result = maxFieldAggregateFunction.getResult(accumulator);

        assertEquals(new Integer(1), result);
    }

    @Test
    public void merge() {
        MaxFieldAggregateFunction<Integer> maxFieldAggregateFunction = new MaxFieldAggregateFunction<>();
        MutableObj<Integer> accumulator1 = maxFieldAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = maxFieldAggregateFunction.createAccumulator();

        maxFieldAggregateFunction.add(1, accumulator1);
        maxFieldAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = maxFieldAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(new Integer(2), merge.get());
    }

}