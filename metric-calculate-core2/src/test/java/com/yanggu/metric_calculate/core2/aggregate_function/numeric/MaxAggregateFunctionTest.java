package com.yanggu.metric_calculate.core2.aggregate_function.numeric;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Numerical;
import org.junit.Test;

import static org.junit.Assert.*;

public class MaxAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = MaxAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("MAX", mergeType.value());
    }

    @Test
    public void testNumerical() {
        Numerical numerical = MaxAggregateFunction.class.getAnnotation(Numerical.class);
        assertFalse(numerical.multiNumber());
    }

    @Test
    public void testConstructor() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        assertNotNull(maxAggregateFunction);
    }

    // Test case for createAccumulator method
    @Test
    public void testCreateAccumulator() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.createAccumulator();
        assertEquals(Double.MIN_VALUE, accumulator, 0.0D);
    }

    // Test case for add method with a positive input
    @Test
    public void testAddWithPositiveInput() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.add(5, 0.0D);
        assertEquals(5.0D, accumulator, 0.0D);
    }

    // Test case for add method with a negative input
    @Test
    public void testAddWithNegativeInput() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.add(-5, 0.0D);
        assertEquals(0.0D, accumulator, 0.0D);
    }

    // Test case for add method with a null input
    @Test
    public void testAddWithNullInput() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.add(null, 0.0D);
        assertEquals(0.0D, accumulator, 0.0D);
    }

    // Test case for getResult method with a positive accumulator
    @Test
    public void testGetResultWithPositiveAccumulator() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.getResult(5.0D);
        assertEquals(5.0D, accumulator, 0.0D);
    }

    // Test case for getResult method with a negative accumulator
    @Test
    public void testGetResultWithNegativeAccumulator() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.getResult(-5.0D);
        assertEquals(-5.0D, accumulator, 0.0D);
    }

    // Test case for merge method with two positive accumulators
    @Test
    public void testMergeWithTwoPositiveAccumulators() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.merge(5.0D, 10.0D);
        assertEquals(10.0D, accumulator, 0.0D);
    }

    // Test case for merge method with two negative accumulators
    @Test
    public void testMergeWithTwoNegativeAccumulators() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.merge(-10.0D, -5.0D);
        assertEquals(-5.0D, accumulator, 0.0D);
    }

    // Test case for merge method with one positive and one negative accumulator
    @Test
    public void testMergeWithOnePositiveAndOneNegativeAccumulator() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.merge(-10.0D, 5.0D);
        assertEquals(5.0D, accumulator, 0.0D);
    }

}