package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlidingCountWindowAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = SlidingCountWindowAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("SLIDINGCOUNTWINDOW", aggregateFunctionAnnotation.name());
    }

    @Test
    void testCollective() {
        Collective collective = SlidingCountWindowAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(1, collective.retainStrategy());
        assertEquals(0, collective.keyStrategy());
    }

    @Test
    void testConstructor() {
        SlidingCountWindowAggregateFunction<Integer, Double, Double> slidingCountWindowAggregateFunction = new SlidingCountWindowAggregateFunction<>();
        assertEquals(Integer.valueOf(10), slidingCountWindowAggregateFunction.getLimit());
        assertNull(slidingCountWindowAggregateFunction.getAggregateFunction());
    }

    @Test
    void createAccumulator() {
        SlidingCountWindowAggregateFunction<Integer, Double, Double> slidingCountWindowAggregateFunction = new SlidingCountWindowAggregateFunction<>();
        List<Integer> accumulator = slidingCountWindowAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
    }

    @Test
    void add() {
        SlidingCountWindowAggregateFunction<Integer, Double, Double> slidingCountWindowAggregateFunction = new SlidingCountWindowAggregateFunction<>();
        slidingCountWindowAggregateFunction.setLimit(2);
        slidingCountWindowAggregateFunction.setAggregateFunction(new SumAggregateFunction<>());

        List<Integer> accumulator = slidingCountWindowAggregateFunction.createAccumulator();
        slidingCountWindowAggregateFunction.add(1, accumulator);
        assertEquals(1, accumulator.size());
        assertEquals(Integer.valueOf(1), accumulator.get(0));

        slidingCountWindowAggregateFunction.add(2, accumulator);
        assertEquals(2, accumulator.size());
        assertEquals(Integer.valueOf(1), accumulator.get(0));
        assertEquals(Integer.valueOf(2), accumulator.get(1));

        slidingCountWindowAggregateFunction.add(3, accumulator);
        assertEquals(2, accumulator.size());
        assertEquals(Integer.valueOf(2), accumulator.get(0));
        assertEquals(Integer.valueOf(3), accumulator.get(1));
    }

    @Test
    void getResult() {
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
    void merge() {
        SlidingCountWindowAggregateFunction<Integer, Double, Double> slidingCountWindowAggregateFunction = new SlidingCountWindowAggregateFunction<>();
        slidingCountWindowAggregateFunction.setLimit(2);

        List<Integer> accumulator1 = slidingCountWindowAggregateFunction.createAccumulator();
        List<Integer> accumulator2 = slidingCountWindowAggregateFunction.createAccumulator();

        accumulator1.add(1);
        accumulator1.add(2);

        accumulator2.add(3);
        accumulator2.add(4);

        List<Integer> merge = slidingCountWindowAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(ListUtil.of(3, 4), merge);
    }

}