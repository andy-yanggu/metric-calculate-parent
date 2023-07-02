package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import cn.hutool.core.collection.BoundedPriorityQueue;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * SortedListObjectAggregateFunction有界有序对象列表单元测试类
 */
public class SortedListObjectAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = SortedListObjectAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("SORTEDLIMITLISTOBJECT", mergeType.value());
    }

    @Test
    public void testCollective() {
        Collective collective = SortedListObjectAggregateFunction.class.getAnnotation(Collective.class);
        assertEquals(2, collective.retainStrategy());
        assertEquals(2, collective.keyStrategy());
    }

    @Test
    public void testConstructor() {
        SortedListObjectAggregateFunction<Integer> sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction<>();
        assertEquals(new Integer(10), sortedListObjectAggregateFunction.getLimit());
    }

    @Test
    public void createAccumulator() {
        SortedListObjectAggregateFunction<Integer> sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction<>();
        BoundedPriorityQueue<Integer> accumulator = sortedListObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
        assertEquals(10, ReflectUtil.getFieldValue(accumulator, "capacity"));
    }

    @Test
    public void testAdd1() {
        SortedListObjectAggregateFunction<Integer> sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction<>();
        //设置为3个
        sortedListObjectAggregateFunction.setLimit(3);
        BoundedPriorityQueue<Integer> accumulator = sortedListObjectAggregateFunction.createAccumulator();

        sortedListObjectAggregateFunction.add(1, accumulator);
        List<Integer> integers = accumulator.toList();
        assertEquals(CollUtil.toList(1), integers);

        sortedListObjectAggregateFunction.add(3, accumulator);
        integers = accumulator.toList();
        assertEquals(CollUtil.toList(1, 3), integers);

        sortedListObjectAggregateFunction.add(2, accumulator);
        integers = accumulator.toList();
        assertEquals(CollUtil.toList(1, 2, 3), integers);

        sortedListObjectAggregateFunction.add(4, accumulator);
        integers = accumulator.toList();
        assertEquals(CollUtil.toList(1, 2, 3), integers);

        sortedListObjectAggregateFunction.add(1, accumulator);
        integers = accumulator.toList();
        assertEquals(CollUtil.toList(1, 1, 2), integers);
    }

    @Test
    public void getResult() {
        SortedListObjectAggregateFunction<Integer> sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction<>();
        BoundedPriorityQueue<Integer> accumulator = sortedListObjectAggregateFunction.createAccumulator();

        sortedListObjectAggregateFunction.add(1, accumulator);

        List<Integer> result = sortedListObjectAggregateFunction.getResult(accumulator);
        assertEquals(CollUtil.toList(1), result);
    }

    @Test
    public void merge() {
        SortedListObjectAggregateFunction<Integer> sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction<>();
        sortedListObjectAggregateFunction.setLimit(3);

        BoundedPriorityQueue<Integer> accumulator1 = sortedListObjectAggregateFunction.createAccumulator();
        BoundedPriorityQueue<Integer> accumulator2 = sortedListObjectAggregateFunction.createAccumulator();

        sortedListObjectAggregateFunction.add(1, accumulator1);
        sortedListObjectAggregateFunction.add(3, accumulator1);
        sortedListObjectAggregateFunction.add(2, accumulator1);
        sortedListObjectAggregateFunction.add(4, accumulator1);

        sortedListObjectAggregateFunction.add(-1, accumulator2);
        sortedListObjectAggregateFunction.add(0, accumulator2);
        sortedListObjectAggregateFunction.add(1, accumulator2);
        sortedListObjectAggregateFunction.add(2, accumulator2);

        List<Integer> result = sortedListObjectAggregateFunction.getResult(sortedListObjectAggregateFunction.merge(accumulator1, accumulator2));

        assertEquals(CollUtil.toList(-1, 0, 1), result);
    }

}