package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * 去重计数单元测试类
 */
@ExtendWith(MockitoExtension.class)
class DistinctCountAggregateFunctionTest {

    @Mock
    private HashSet<Integer> mock;

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = DistinctCountAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("DISTINCTCOUNT", aggregateFunctionAnnotation.name());
    }

    @Test
    void testCollective() {
        Collective collective = DistinctCountAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(0, collective.retainStrategy());
        assertEquals(1, collective.keyStrategy());
    }

    @Test
    void testConstructor() {
        DistinctCountAggregateFunction<Object> distinctCountAggregateFunction = new DistinctCountAggregateFunction<>();
        assertNotNull(distinctCountAggregateFunction);
    }

    @Test
    void testCreateAccumulator() {
        DistinctCountAggregateFunction<Integer> distinctCount = new DistinctCountAggregateFunction<>();
        Set<Integer> accumulator = distinctCount.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
    }

    @Test
    void testAddElement() {
        DistinctCountAggregateFunction<Integer> distinctCount = new DistinctCountAggregateFunction<>();
        Set<Integer> accumulator = new HashSet<>();
        accumulator = distinctCount.add(1, accumulator);
        assertNotNull(accumulator);
        assertEquals(1, accumulator.size());
        assertTrue(accumulator.contains(1));
    }

    @Test
    void testAddDuplicate() {
        DistinctCountAggregateFunction<Integer> distinctCount = new DistinctCountAggregateFunction<>();
        Set<Integer> accumulator = new HashSet<>();
        accumulator = distinctCount.add(1, accumulator);
        accumulator = distinctCount.add(1, accumulator);
        assertNotNull(accumulator);
        assertEquals(1, accumulator.size());
        assertTrue(accumulator.contains(1));
    }

    @Test
    void testGetResult() {
        DistinctCountAggregateFunction<Integer> distinctCount = new DistinctCountAggregateFunction<>();
        when(mock.size()).thenReturn(1);
        Integer result = distinctCount.getResult(mock);
        assertEquals(Integer.valueOf(1), result);
        //验证依赖方法
        verify(mock, times(1)).size();
    }

    @Test
    void testMergeAccumulators() {
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
    void testMergeDuplicateElements() {
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