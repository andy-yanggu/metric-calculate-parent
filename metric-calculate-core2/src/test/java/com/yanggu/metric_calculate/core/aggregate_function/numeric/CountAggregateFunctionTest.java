package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CountAggregateFunction.
 */
class CountAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = CountAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("COUNT", mergeType.value());
    }

    @Test
    void testNumerical() {
        Numerical numerical = CountAggregateFunction.class.getAnnotation(Numerical.class);
        assertFalse(numerical.multiNumber());
    }

    @Test
    void testConstructor() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        assertNotNull(countAggregateFunction);
    }
    
    @Test
    void testCreateAccumulator() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.createAccumulator();
        assertEquals(0L, (long) accumulator);
    }

    @Test
    void testAdd() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.add(1, 0L);
        assertEquals(1L, (long) accumulator);
        accumulator = countAggregateFunction.add(null, 0L);
        assertEquals(0L, (long) accumulator);
    }

    @Test
    void testGetResult() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.getResult(10L);
        assertEquals(10L, (long) accumulator);
    }

    @Test
    void testMerge() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        Long accumulator = countAggregateFunction.merge(10L, 20L);
        assertEquals(30L, (long) accumulator);
    }

}