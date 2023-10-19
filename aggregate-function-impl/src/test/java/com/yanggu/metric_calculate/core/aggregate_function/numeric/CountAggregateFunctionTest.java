package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for CountAggregateFunction.
 */
class CountAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(CountAggregateFunction.class, "COUNT");
    }

    @Test
    void testNumerical() {
        AggregateFunctionTestBase.testNumerical(CountAggregateFunction.class, false);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(CountAggregateFunction.class);
    }
    
    @Test
    void testCreateAccumulator() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.createAccumulator();
        assertEquals(0L, accumulator);
    }

    @Test
    void testAdd() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.add(1, 0L);
        assertEquals(1L, accumulator);
        accumulator = countAggregateFunction.add(null, 0L);
        assertEquals(0L, accumulator);
    }

    @Test
    void testGetResult() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.getResult(10L);
        assertEquals(10L, accumulator);
    }

    @Test
    void testMerge() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.merge(10L, 20L);
        assertEquals(30L, accumulator);
    }

}