package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SumAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(SumAggregateFunction.class, "SUM");
    }

    @Test
    void testNumerical() {
        AggregateFunctionTestBase.testNumerical(SumAggregateFunction.class, false);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(SumAggregateFunction.class);
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