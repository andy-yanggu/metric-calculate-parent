package com.yanggu.metric_calculate.core2.aggregate_function.numeric;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for CountAggregateFunction.
 */
public class CountAggregateFunctionTest {

    @Test
    public void testCreateAccumulator() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.createAccumulator();
        assertEquals(0L, (long) accumulator);
    }

    @Test
    public void testAdd() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.add(1, 0L);
        assertEquals(1L, (long) accumulator);
        accumulator = countAggregateFunction.add(null, 0L);
        assertEquals(0L, (long) accumulator);
    }

    @Test
    public void testGetResult() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.getResult(10L);
        assertEquals(10L, (long) accumulator);
    }

    @Test
    public void testMerge() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.merge(10L, 20L);
        assertEquals(30L, (long) accumulator);
    }

}