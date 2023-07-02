package com.yanggu.metric_calculate.core2.aggregate_function.numeric;

import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Numerical;
import org.junit.Test;

import static org.junit.Assert.*;

public class SumAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = SumAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("SUM", mergeType.value());
    }

    @Test
    public void testNumerical() {
        Numerical numerical = SumAggregateFunction.class.getAnnotation(Numerical.class);
        assertFalse(numerical.multiNumber());
    }

    @Test
    public void testConstructor() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        assertNotNull(sumAggregateFunction);
    }

    @Test
    public void testCreateAccumulator() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double accumulator = sumAggregateFunction.createAccumulator();
        assertEquals(0.0D, accumulator, 0.0D);
    }

    @Test
    public void testAddPositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double accumulator = sumAggregateFunction.add(1, 0.0D);
        assertEquals(1.0D, accumulator, 0.0D);
    }

    @Test
    public void testAddNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double accumulator = sumAggregateFunction.add(-1, 0.0D);
        assertEquals(-1.0D, accumulator, 0.0D);
    }

    @Test
    public void testGetResultPositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.getResult(1.0D);
        assertEquals(1.0D, result, 0.0D);
    }

    @Test
    public void testGetResultNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.getResult(-1.0D);
        assertEquals(-1.0D, result, 0.0D);
    }

    @Test
    public void testMergePositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.merge(1.0D, 2.0D);
        assertEquals(3.0D, result, 0.0D);
    }

    @Test
    public void testMergeNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.merge(-1.0D, -2.0D);
        assertEquals(-3.0D, result, 0.0D);
    }

}