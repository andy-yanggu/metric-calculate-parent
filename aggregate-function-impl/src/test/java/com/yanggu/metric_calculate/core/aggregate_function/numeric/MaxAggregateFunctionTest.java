package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaxAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = MaxAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("MAX", aggregateFunctionAnnotation.name());
    }

    @Test
    void testNumerical() {
        Numerical numerical = MaxAggregateFunction.class.getAnnotation(Numerical.class);
        assertFalse(numerical.multiNumber());
    }

    @Test
    void testConstructor() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        assertNotNull(maxAggregateFunction);
    }

    // Test case for createAccumulator method
    @Test
    void testCreateAccumulator() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.createAccumulator();
        assertEquals(Double.MIN_VALUE, accumulator, 0.0D);
    }

    // Test case for add method with a positive input
    @Test
    void testAddWithPositiveInput() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.add(5, 0.0D);
        assertEquals(5.0D, accumulator, 0.0D);
    }

    // Test case for add method with a negative input
    @Test
    void testAddWithNegativeInput() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.add(-5, 0.0D);
        assertEquals(0.0D, accumulator, 0.0D);
    }

    // Test case for add method with a null input
    @Test
    void testAddWithNullInput() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.add(null, 0.0D);
        assertEquals(0.0D, accumulator, 0.0D);
    }

    // Test case for getResult method with a positive accumulator
    @Test
    void testGetResultWithPositiveAccumulator() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.getResult(5.0D);
        assertEquals(5.0D, accumulator, 0.0D);
    }

    // Test case for getResult method with a negative accumulator
    @Test
    void testGetResultWithNegativeAccumulator() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.getResult(-5.0D);
        assertEquals(-5.0D, accumulator, 0.0D);
    }

    // Test case for merge method with two positive accumulators
    @Test
    void testMergeWithTwoPositiveAccumulators() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.merge(5.0D, 10.0D);
        assertEquals(10.0D, accumulator, 0.0D);
    }

    // Test case for merge method with two negative accumulators
    @Test
    void testMergeWithTwoNegativeAccumulators() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.merge(-10.0D, -5.0D);
        assertEquals(-5.0D, accumulator, 0.0D);
    }

    // Test case for merge method with one positive and one negative accumulator
    @Test
    void testMergeWithOnePositiveAndOneNegativeAccumulator() {
        MaxAggregateFunction<Integer> maxAggregateFunction = new MaxAggregateFunction<>();
        Double accumulator = maxAggregateFunction.merge(-10.0D, 5.0D);
        assertEquals(5.0D, accumulator, 0.0D);
    }

}