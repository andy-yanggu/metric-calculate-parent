package com.yanggu.metric_calculate.core2.aggregate_function.numeric;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SumAggregateFunctionTest {

    @Test
    public void testCreateAccumulator() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double accumulator = sumAggregateFunction.createAccumulator();
        assertEquals(0.0D, accumulator, 0.0);
    }

    @Test
    public void testAddPositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double accumulator = sumAggregateFunction.add(1, 0.0D);
        assertEquals(1.0D, accumulator, 0.0);
    }

    @Test
    public void testAddNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double accumulator = sumAggregateFunction.add(-1, 0.0D);
        assertEquals(-1.0D, accumulator, 0.0);
    }

    @Test
    public void testGetResultPositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.getResult(1.0D);
        assertEquals(1.0D, result, 0.0);
    }

    @Test
    public void testGetResultNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.getResult(-1.0D);
        assertEquals(-1.0D, result, 0.0);
    }

    @Test
    public void testMergePositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.merge(1.0D, 2.0D);
        assertEquals(3.0D, result, 0.0);
    }

    @Test
    public void testMergeNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.merge(-1.0D, -2.0D);
        assertEquals(-3.0D, result, 0.0);
    }

}