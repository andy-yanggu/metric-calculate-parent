package com.yanggu.metric_calculate.core2.aggregate_function.numeric;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Numerical;
import org.junit.Test;

import static org.junit.Assert.*;

public class MinAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = MinAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("MIN", mergeType.value());
    }

    @Test
    public void testNumerical() {
        Numerical numerical = MinAggregateFunction.class.getAnnotation(Numerical.class);
        assertFalse(numerical.multiNumber());
    }

    @Test
    public void testConstructor() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        assertNotNull(minAggregateFunction);
    }

    @Test
    public void testCreateAccumulator() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        assertEquals(Double.MAX_VALUE, accumulator, 0.0D);
    }

    @Test
    public void testAddPositive() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        Double result = minAggregateFunction.add(1, accumulator);
        assertEquals(1.0D, result, 0.0D);
    }

    @Test
    public void testAddNegative() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        Double result = minAggregateFunction.add(-1, accumulator);
        assertEquals(-1.0D, result, 0.0D);
    }

    @Test
    public void testGetResultPositive() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        Double result = minAggregateFunction.getResult(accumulator);
        assertEquals(Double.MAX_VALUE, result, 0.0D);
    }

    @Test
    public void testGetResultNegative() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.add(-1, minAggregateFunction.createAccumulator());
        Double result = minAggregateFunction.getResult(accumulator);
        assertEquals(-1.0D, result, 0.0D);
    }

    @Test
    public void testMergePositive() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double thisAccumulator = minAggregateFunction.add(1, minAggregateFunction.createAccumulator());
        Double thatAccumulator = minAggregateFunction.add(2, minAggregateFunction.createAccumulator());
        Double result = minAggregateFunction.merge(thisAccumulator, thatAccumulator);
        assertEquals(1.0D, result, 0.0D);
    }

    @Test
    public void testMergeNegative() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double thisAccumulator = minAggregateFunction.add(-1, minAggregateFunction.createAccumulator());
        Double thatAccumulator = minAggregateFunction.add(-2, minAggregateFunction.createAccumulator());
        Double result = minAggregateFunction.merge(thisAccumulator, thatAccumulator);
        assertEquals(-2.0D, result, 0.0D);
    }
}