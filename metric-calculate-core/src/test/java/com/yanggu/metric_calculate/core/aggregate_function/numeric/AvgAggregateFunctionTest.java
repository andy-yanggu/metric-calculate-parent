package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AvgAggregateFunction单元测试类
 */
class AvgAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = AvgAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("AVG", mergeType.value());
    }

    @Test
    void testNumerical() {
        Numerical numerical = AvgAggregateFunction.class.getAnnotation(Numerical.class);
        assertFalse(numerical.multiNumber());
    }

    @Test
    void testConstructor() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        assertNotNull(avgAggregateFunction);
    }

    @Test
    void testCreateAccumulator() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutableEntry<Double, Long> accumulator = avgAggregateFunction.createAccumulator();
        assertEquals(0.0D, accumulator.getKey(), 0.0);
        assertEquals(0L, accumulator.getValue().longValue());
    }

    @Test
    void testAdd() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutableEntry<Double, Long> accumulator = avgAggregateFunction.createAccumulator();
        accumulator = avgAggregateFunction.add(1, accumulator);
        assertEquals(1.0D, accumulator.getKey(), 0.0);
        assertEquals(1L, accumulator.getValue().longValue());
    }

    @Test
    void testAddNegative() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutableEntry<Double, Long> accumulator = avgAggregateFunction.createAccumulator();
        accumulator = avgAggregateFunction.add(-1, accumulator);
        assertEquals(-1.0D, accumulator.getKey(), 0.0);
        assertEquals(1L, accumulator.getValue().longValue());
    }

    @Test
    void testGetResult() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutableEntry<Double, Long> accumulator = avgAggregateFunction.createAccumulator();
        accumulator = avgAggregateFunction.add(1, accumulator);
        accumulator = avgAggregateFunction.add(2, accumulator);
        Double result = avgAggregateFunction.getResult(accumulator);
        assertEquals(1.5D, result, 0.0);
    }

    @Test
    void testGetResultNegative() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutableEntry<Double, Long> accumulator = avgAggregateFunction.createAccumulator();
        accumulator = avgAggregateFunction.add(-1, accumulator);
        accumulator = avgAggregateFunction.add(-2, accumulator);
        Double result = avgAggregateFunction.getResult(accumulator);
        assertEquals(-1.5D, result, 0.0);
    }

    @Test
    void testMerge() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutableEntry<Double, Long> thisAccumulator = avgAggregateFunction.createAccumulator();
        thisAccumulator = avgAggregateFunction.add(1, thisAccumulator);
        MutableEntry<Double, Long> thatAccumulator = avgAggregateFunction.createAccumulator();
        thatAccumulator = avgAggregateFunction.add(2, thatAccumulator);
        thisAccumulator = avgAggregateFunction.merge(thisAccumulator, thatAccumulator);
        assertEquals(3.0D, thisAccumulator.getKey(), 0.0);
        assertEquals(2L, thisAccumulator.getValue().longValue());
    }

    @Test
    void testMergeNegative() {
        AvgAggregateFunction<Integer> avgAggregateFunction = new AvgAggregateFunction<>();
        MutableEntry<Double, Long> thisAccumulator = avgAggregateFunction.createAccumulator();
        thisAccumulator = avgAggregateFunction.add(-1, thisAccumulator);
        MutableEntry<Double, Long> thatAccumulator = avgAggregateFunction.createAccumulator();
        thatAccumulator = avgAggregateFunction.add(-2, thatAccumulator);
        thisAccumulator = avgAggregateFunction.merge(thisAccumulator, thatAccumulator);
        assertEquals(-3.0D, thisAccumulator.getKey(), 0.0);
        assertEquals(2L, thisAccumulator.getValue().longValue());
    }

}