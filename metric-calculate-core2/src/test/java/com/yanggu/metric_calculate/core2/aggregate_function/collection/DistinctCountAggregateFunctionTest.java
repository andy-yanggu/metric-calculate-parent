package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * 去重计数单元测试类
 */
public class DistinctCountAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = DistinctCountAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("DISTINCTCOUNT", mergeType.value());
    }

    @Test
    public void testCollective() {
        Collective collective = DistinctCountAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(0, collective.retainStrategy());
        assertEquals(1, collective.keyStrategy());
    }

    @Test
    public void testConstructor() {
        DistinctCountAggregateFunction<Object> distinctCountAggregateFunction = new DistinctCountAggregateFunction<>();
        assertNotNull(distinctCountAggregateFunction);
    }

    @Test
    public void testCreateAccumulator() {
        DistinctCountAggregateFunction<Integer> distinctCount = new DistinctCountAggregateFunction<>();
        Set<Integer> accumulator = distinctCount.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
    }

    @Test
    public void testAddElement() {
        DistinctCountAggregateFunction<Integer> distinctCount = new DistinctCountAggregateFunction<>();
        Set<Integer> accumulator = new HashSet<>();
        accumulator = distinctCount.add(1, accumulator);
        assertNotNull(accumulator);
        assertEquals(1, accumulator.size());
        assertTrue(accumulator.contains(1));
    }

    @Test
    public void testAddDuplicate() {
        DistinctCountAggregateFunction<Integer> distinctCount = new DistinctCountAggregateFunction<>();
        Set<Integer> accumulator = new HashSet<>();
        accumulator = distinctCount.add(1, accumulator);
        accumulator = distinctCount.add(1, accumulator);
        assertNotNull(accumulator);
        assertEquals(1, accumulator.size());
        assertTrue(accumulator.contains(1));
    }

    @Test
    public void testGetResult() {
        DistinctCountAggregateFunction<Integer> distinctCount = new DistinctCountAggregateFunction<>();
        HashSet<Integer> mock = Mockito.mock(HashSet.class);
        Mockito.when(mock.size()).thenReturn(1);
        Integer result = distinctCount.getResult(mock);
        assertEquals(Integer.valueOf(1), result);
        //验证依赖方法
        Mockito.verify(mock, Mockito.times(1)).size();
    }

    @Test
    public void testMergeAccumulators() {
        DistinctCountAggregateFunction<Integer> distinctCount = new DistinctCountAggregateFunction<>();
        Set<Integer> accumulator1 = new HashSet<>();
        Set<Integer> accumulator2 = new HashSet<>();
        accumulator1.add(1);
        accumulator2.add(2);
        Set<Integer> merged = distinctCount.merge(accumulator1, accumulator2);
        assertNotNull(merged);
        assertEquals(2, merged.size());
        assertTrue(merged.contains(1));
        assertTrue(merged.contains(2));
    }

    @Test
    public void testMergeDuplicateElements() {
        DistinctCountAggregateFunction<Integer> distinctCount = new DistinctCountAggregateFunction<>();
        Set<Integer> accumulator1 = new HashSet<>();
        Set<Integer> accumulator2 = new HashSet<>();
        accumulator1.add(1);
        accumulator2.add(1);
        accumulator2.add(2);
        Set<Integer> merged = distinctCount.merge(accumulator1, accumulator2);
        assertNotNull(merged);
        assertEquals(2, merged.size());
        assertTrue(merged.contains(1));
        assertTrue(merged.contains(2));
    }

}