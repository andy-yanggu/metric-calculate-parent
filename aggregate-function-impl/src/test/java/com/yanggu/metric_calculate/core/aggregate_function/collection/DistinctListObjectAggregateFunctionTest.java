package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 去重对象列表单元测试类
 */
class DistinctListObjectAggregateFunctionTest {

    @Test
    void testMergeType() {
        MergeType mergeType = DistinctListObjectAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("DISTINCTLISTOBJECT", mergeType.value());
    }

    @Test
    void testCollective() {
        Collective collective = DistinctListObjectAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(2, collective.retainStrategy());
        assertEquals(1, collective.keyStrategy());
    }

    @Test
    void testConstructor() {
        DistinctListObjectAggregateFunction<Object> distinctListObjectAggregateFunction = new DistinctListObjectAggregateFunction<>();
        assertNotNull(distinctListObjectAggregateFunction);
    }
    
    /**
     * 测试创建累加器是否正常
     */
    @Test
    void testCreateAccumulator() {
        DistinctListObjectAggregateFunction<Integer> distinctListObjectAggregateFunction = new DistinctListObjectAggregateFunction<>();
        Set<Integer> accumulator = distinctListObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator); // 确认累加器不为空
        assertTrue(accumulator instanceof HashSet); // 确认累加器是HashSet类型
    }

    /**
     * 测试添加元素是否正常
     */
    @Test
    void testAdd() {
        DistinctListObjectAggregateFunction<Integer> distinctListObjectAggregateFunction = new DistinctListObjectAggregateFunction<>();
        Set<Integer> accumulator = distinctListObjectAggregateFunction.createAccumulator();
        accumulator = distinctListObjectAggregateFunction.add(1, accumulator);
        accumulator = distinctListObjectAggregateFunction.add(1, accumulator); // 添加重复的元素
        assertTrue(accumulator.contains(1)); // 确认元素只被添加了一次
        assertEquals(1, accumulator.size()); // 确认累加器中只包含一个元素
    }

    /**
     * 测试获取结果是否正常
     */
    @Test
    void testGetResult() {
        DistinctListObjectAggregateFunction<Integer> distinctListObjectAggregateFunction = new DistinctListObjectAggregateFunction<>();
        Set<Integer> accumulator = distinctListObjectAggregateFunction.createAccumulator();
        accumulator = distinctListObjectAggregateFunction.add(1, accumulator);
        List<Integer> result = distinctListObjectAggregateFunction.getResult(accumulator);
        assertTrue(result.contains(1)); // 确认结果列表中包含已添加的元素
        assertTrue(result instanceof ArrayList); // 确认结果列表是ArrayList类型
    }

    /**
     * 测试合并累加器是否正常
     */
    @Test
    void testMerge() {
        DistinctListObjectAggregateFunction<Integer> distinctListObjectAggregateFunction = new DistinctListObjectAggregateFunction<>();
        Set<Integer> thisAccumulator = distinctListObjectAggregateFunction.createAccumulator();
        thisAccumulator = distinctListObjectAggregateFunction.add(1, thisAccumulator);
        Set<Integer> thatAccumulator = distinctListObjectAggregateFunction.createAccumulator();
        thatAccumulator = distinctListObjectAggregateFunction.add(2, thatAccumulator);
        thatAccumulator = distinctListObjectAggregateFunction.add(1, thatAccumulator); // 向第二个累加器添加重复元素
        thisAccumulator = distinctListObjectAggregateFunction.merge(thisAccumulator, thatAccumulator);
        assertTrue(thisAccumulator.contains(1)); // 确认累加器中包含第一个累加器中的元素
        assertTrue(thisAccumulator.contains(2)); // 确认累加器中包含第一个累加器中的元素
        assertEquals(2, thisAccumulator.size()); // 确认累加器中只有两个元素
    }

}