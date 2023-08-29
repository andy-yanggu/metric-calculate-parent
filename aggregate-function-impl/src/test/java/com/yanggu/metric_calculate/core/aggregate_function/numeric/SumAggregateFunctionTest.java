package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SumAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = SumAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("SUM", aggregateFunctionAnnotation.name());
    }

    @Test
    void testNumerical() {
        Numerical numerical = SumAggregateFunction.class.getAnnotation(Numerical.class);
        assertFalse(numerical.multiNumber());
    }

    @Test
    void testConstructor() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        assertNotNull(sumAggregateFunction);
    }

    @Test
    void testCreateAccumulator() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double accumulator = sumAggregateFunction.createAccumulator();
        assertEquals(0.0D, accumulator, 0.0D);
    }

    @Test
    void testAddPositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double accumulator = sumAggregateFunction.add(1, 0.0D);
        assertEquals(1.0D, accumulator, 0.0D);
    }

    @Test
    void testAddNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double accumulator = sumAggregateFunction.add(-1, 0.0D);
        assertEquals(-1.0D, accumulator, 0.0D);
    }

    @Test
    void testGetResultPositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.getResult(1.0D);
        assertEquals(1.0D, result, 0.0D);
    }

    @Test
    void testGetResultNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.getResult(-1.0D);
        assertEquals(-1.0D, result, 0.0D);
    }

    @Test
    void testMergePositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.merge(1.0D, 2.0D);
        assertEquals(3.0D, result, 0.0D);
    }

    @Test
    void testMergeNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double result = sumAggregateFunction.merge(-1.0D, -2.0D);
        assertEquals(-3.0D, result, 0.0D);
    }

}