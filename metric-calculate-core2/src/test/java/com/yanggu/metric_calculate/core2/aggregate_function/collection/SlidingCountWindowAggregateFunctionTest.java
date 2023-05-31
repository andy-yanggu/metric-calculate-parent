package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core2.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SlidingCountWindowAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = SlidingCountWindowAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("SLIDINGCOUNTWINDOW", mergeType.value());
    }

    @Test
    public void testCollective() {
        Collective collective = SlidingCountWindowAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(1, collective.retainStrategy());
        assertFalse(collective.useSortedField());
        assertFalse(collective.useDistinctField());
    }

    @Test
    public void testConstructor() {
        SlidingCountWindowAggregateFunction<Integer, Double, Double> slidingCountWindowAggregateFunction = new SlidingCountWindowAggregateFunction<>();
        assertEquals(new Integer(10), slidingCountWindowAggregateFunction.getLimit());
        assertNull(slidingCountWindowAggregateFunction.getAggregateFunction());
    }

    @Test
    public void createAccumulator() {
        SlidingCountWindowAggregateFunction<Integer, Double, Double> slidingCountWindowAggregateFunction = new SlidingCountWindowAggregateFunction<>();
        List<Integer> accumulator = slidingCountWindowAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
    }

    @Test
    public void add() {
        SlidingCountWindowAggregateFunction<Integer, Double, Double> slidingCountWindowAggregateFunction = new SlidingCountWindowAggregateFunction<>();
        slidingCountWindowAggregateFunction.setLimit(2);
        slidingCountWindowAggregateFunction.setAggregateFunction(new SumAggregateFunction<>());

        List<Integer> accumulator = slidingCountWindowAggregateFunction.createAccumulator();
        slidingCountWindowAggregateFunction.add(1, accumulator);
        assertEquals(1, accumulator.size());
        assertEquals(new Integer(1), accumulator.get(0));

        slidingCountWindowAggregateFunction.add(2, accumulator);
        assertEquals(2, accumulator.size());
        assertEquals(new Integer(1), accumulator.get(0));
        assertEquals(new Integer(2), accumulator.get(1));

        slidingCountWindowAggregateFunction.add(3, accumulator);
        assertEquals(2, accumulator.size());
        assertEquals(new Integer(2), accumulator.get(0));
        assertEquals(new Integer(3), accumulator.get(1));
    }

    @Test
    public void getResult() {
        SlidingCountWindowAggregateFunction<Integer, Double, Double> slidingCountWindowAggregateFunction = new SlidingCountWindowAggregateFunction<>();
        slidingCountWindowAggregateFunction.setLimit(2);
        slidingCountWindowAggregateFunction.setAggregateFunction(new SumAggregateFunction<>());

        List<Integer> accumulator = slidingCountWindowAggregateFunction.createAccumulator();
        slidingCountWindowAggregateFunction.add(1, accumulator);
        slidingCountWindowAggregateFunction.add(2, accumulator);

        Double result = slidingCountWindowAggregateFunction.getResult(accumulator);
        assertEquals(3.0D, result, 0.0D);
    }

    @Test
    public void merge() {
        SlidingCountWindowAggregateFunction<Integer, Double, Double> slidingCountWindowAggregateFunction = new SlidingCountWindowAggregateFunction<>();
        slidingCountWindowAggregateFunction.setLimit(2);

        List<Integer> accumulator1 = slidingCountWindowAggregateFunction.createAccumulator();
        List<Integer> accumulator2 = slidingCountWindowAggregateFunction.createAccumulator();

        accumulator1.add(1);
        accumulator1.add(2);

        accumulator2.add(3);
        accumulator2.add(4);

        List<Integer> merge = slidingCountWindowAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(CollUtil.toList(3, 4), merge);
    }

}