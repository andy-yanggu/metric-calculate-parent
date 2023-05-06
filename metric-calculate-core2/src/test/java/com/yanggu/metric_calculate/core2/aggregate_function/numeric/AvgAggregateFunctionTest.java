package com.yanggu.metric_calculate.core2.aggregate_function.numeric;

import cn.hutool.core.lang.mutable.MutablePair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * AvgAggregateFunction单元测试类
 */
 public class AvgAggregateFunctionTest {

     @Test
    public void testCreateAccumulator() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutablePair<Double, Long> accumulator = avgAggregateFunction.createAccumulator();
        assertEquals(0.0D, accumulator.getKey(), 0.0);
        assertEquals(0L, accumulator.getValue().longValue());
    }

     @Test
    public void testAdd() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutablePair<Double, Long> accumulator = avgAggregateFunction.createAccumulator();
        accumulator = avgAggregateFunction.add(1, accumulator);
        assertEquals(1.0D, accumulator.getKey(), 0.0);
        assertEquals(1L, accumulator.getValue().longValue());
    }

     @Test
    public void testAddNegative() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutablePair<Double, Long> accumulator = avgAggregateFunction.createAccumulator();
        accumulator = avgAggregateFunction.add(-1, accumulator);
        assertEquals(-1.0D, accumulator.getKey(), 0.0);
        assertEquals(1L, accumulator.getValue().longValue());
    }

     @Test
    public void testGetResult() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutablePair<Double, Long> accumulator = avgAggregateFunction.createAccumulator();
        accumulator = avgAggregateFunction.add(1, accumulator);
        accumulator = avgAggregateFunction.add(2, accumulator);
        Double result = avgAggregateFunction.getResult(accumulator);
        assertEquals(1.5D, result, 0.0);
    }

     @Test
    public void testGetResultNegative() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutablePair<Double, Long> accumulator = avgAggregateFunction.createAccumulator();
        accumulator = avgAggregateFunction.add(-1, accumulator);
        accumulator = avgAggregateFunction.add(-2, accumulator);
        Double result = avgAggregateFunction.getResult(accumulator);
        assertEquals(-1.5D, result, 0.0);
    }

     @Test
    public void testMerge() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutablePair<Double, Long> thisAccumulator = avgAggregateFunction.createAccumulator();
        thisAccumulator = avgAggregateFunction.add(1, thisAccumulator);
        MutablePair<Double, Long> thatAccumulator = avgAggregateFunction.createAccumulator();
        thatAccumulator = avgAggregateFunction.add(2, thatAccumulator);
        thisAccumulator = avgAggregateFunction.merge(thisAccumulator, thatAccumulator);
        assertEquals(3.0D, thisAccumulator.getKey(), 0.0);
        assertEquals(2L, thisAccumulator.getValue().longValue());
    }

     @Test
    public void testMergeNegative() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutablePair<Double, Long> thisAccumulator = avgAggregateFunction.createAccumulator();
        thisAccumulator = avgAggregateFunction.add(-1, thisAccumulator);
        MutablePair<Double, Long> thatAccumulator = avgAggregateFunction.createAccumulator();
        thatAccumulator = avgAggregateFunction.add(-2, thatAccumulator);
        thisAccumulator = avgAggregateFunction.merge(thisAccumulator, thatAccumulator);
        assertEquals(-3.0D, thisAccumulator.getKey(), 0.0);
        assertEquals(2L, thisAccumulator.getValue().longValue());
    }

}