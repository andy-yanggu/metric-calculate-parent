package com.yanggu.metric_calculate.core2.aggregate_function.numeric;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Numerical;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for CountAggregateFunction.
 */
public class CountAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = CountAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("COUNT", mergeType.value());
    }

    @Test
    public void testNumerical() {
        Numerical numerical = CountAggregateFunction.class.getAnnotation(Numerical.class);
        assertFalse(numerical.multiNumber());
    }

    @Test
    public void testConstructor() {
        CountAggregateFunction<Integer> countAggregateFunction = new CountAggregateFunction<>();
        assertNotNull(countAggregateFunction);
    }
    
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