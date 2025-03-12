package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("求和函数测试")
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
        BigDecimal accumulator = sumAggregateFunction.createAccumulator();
        assertEquals(0.0D, accumulator.doubleValue(), 0.0D);
    }

    @Test
    void testAddPositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        BigDecimal accumulator = sumAggregateFunction.add(1, BigDecimal.ZERO);
        assertEquals(1.0D, accumulator.doubleValue(), 0.0D);
    }

    @Test
    void testAddNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        BigDecimal accumulator = sumAggregateFunction.add(-1, BigDecimal.ZERO);
        assertEquals(-1.0D, accumulator.doubleValue(), 0.0D);
    }

    @Test
    void testGetResultPositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        BigDecimal result = sumAggregateFunction.getResult(BigDecimal.valueOf(1.0D));
        assertEquals(1.0D, result.doubleValue(), 0.0D);
    }

    @Test
    void testGetResultNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        BigDecimal result = sumAggregateFunction.getResult(BigDecimal.valueOf(-1.0D));
        assertEquals(-1.0D, result.doubleValue(), 0.0D);
    }

    @Test
    void testMergePositive() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        BigDecimal result = sumAggregateFunction.merge(BigDecimal.valueOf(1.0D), BigDecimal.valueOf(2.0D));
        assertEquals(BigDecimal.valueOf(3.0D), result);
    }

    @Test
    void testMergeNegative() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        BigDecimal result = sumAggregateFunction.merge(BigDecimal.valueOf(-1.0D), BigDecimal.valueOf(-2.0D));
        assertEquals(BigDecimal.valueOf(-3.0D), result);
    }

}