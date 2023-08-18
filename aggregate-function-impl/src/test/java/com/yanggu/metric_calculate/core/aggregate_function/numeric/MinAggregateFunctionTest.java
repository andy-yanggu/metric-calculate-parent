package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = MinAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("MIN", mergeType.value());
    }

    @Test
    void testNumerical() {
        Numerical numerical = MinAggregateFunction.class.getAnnotation(Numerical.class);
        assertFalse(numerical.multiNumber());
    }

    @Test
    void testConstructor() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        assertNotNull(minAggregateFunction);
    }

    @Test
    void testCreateAccumulator() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        assertEquals(Double.MAX_VALUE, accumulator, 0.0D);
    }

    @Test
    void testAddPositive() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        Double result = minAggregateFunction.add(1, accumulator);
        assertEquals(1.0D, result, 0.0D);
    }

    @Test
    void testAddNegative() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        Double result = minAggregateFunction.add(-1, accumulator);
        assertEquals(-1.0D, result, 0.0D);
    }

    @Test
    void testGetResultPositive() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        Double result = minAggregateFunction.getResult(accumulator);
        assertEquals(Double.MAX_VALUE, result, 0.0D);
    }

    @Test
    void testGetResultNegative() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.add(-1, minAggregateFunction.createAccumulator());
        Double result = minAggregateFunction.getResult(accumulator);
        assertEquals(-1.0D, result, 0.0D);
    }

    @Test
    void testMergePositive() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double thisAccumulator = minAggregateFunction.add(1, minAggregateFunction.createAccumulator());
        Double thatAccumulator = minAggregateFunction.add(2, minAggregateFunction.createAccumulator());
        Double result = minAggregateFunction.merge(thisAccumulator, thatAccumulator);
        assertEquals(1.0D, result, 0.0D);
    }

    @Test
    void testMergeNegative() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double thisAccumulator = minAggregateFunction.add(-1, minAggregateFunction.createAccumulator());
        Double thatAccumulator = minAggregateFunction.add(-2, minAggregateFunction.createAccumulator());
        Double result = minAggregateFunction.merge(thisAccumulator, thatAccumulator);
        assertEquals(-2.0D, result, 0.0D);
    }
}