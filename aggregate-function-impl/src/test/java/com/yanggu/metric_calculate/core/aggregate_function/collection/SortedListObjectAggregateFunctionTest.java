package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.queue.BoundedPriorityQueue;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SortedListObjectAggregateFunction有界有序对象列表单元测试类
 */
class SortedListObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(SortedListObjectAggregateFunction.class, "SORTEDLIMITLISTOBJECT");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(SortedListObjectAggregateFunction.class, 2, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(SortedListObjectAggregateFunction.class);
    }

    @Test
    void createAccumulator() {
        SortedListObjectAggregateFunction<Integer> sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction<>();
        BoundedPriorityQueue<Integer> accumulator = sortedListObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
        assertEquals(10, FieldUtil.getFieldValue(accumulator, "capacity"));
    }

    @Test
    void testAdd1() {
        SortedListObjectAggregateFunction<Integer> sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction<>();
        //设置为3个
        sortedListObjectAggregateFunction.setLimit(3);
        BoundedPriorityQueue<Integer> accumulator = sortedListObjectAggregateFunction.createAccumulator();

        sortedListObjectAggregateFunction.add(1, accumulator);
        List<Integer> integers = accumulator.toList();
        assertEquals(ListUtil.of(1), integers);

        sortedListObjectAggregateFunction.add(3, accumulator);
        integers = accumulator.toList();
        assertEquals(ListUtil.of(1, 3), integers);

        sortedListObjectAggregateFunction.add(2, accumulator);
        integers = accumulator.toList();
        assertEquals(ListUtil.of(1, 2, 3), integers);

        sortedListObjectAggregateFunction.add(4, accumulator);
        integers = accumulator.toList();
        assertEquals(ListUtil.of(1, 2, 3), integers);

        sortedListObjectAggregateFunction.add(1, accumulator);
        integers = accumulator.toList();
        assertEquals(ListUtil.of(1, 1, 2), integers);
    }

    @Test
    void getResult() {
        SortedListObjectAggregateFunction<Integer> sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction<>();
        BoundedPriorityQueue<Integer> accumulator = sortedListObjectAggregateFunction.createAccumulator();

        sortedListObjectAggregateFunction.add(1, accumulator);

        List<Integer> result = sortedListObjectAggregateFunction.getResult(accumulator);
        assertEquals(ListUtil.of(1), result);
    }

    @Test
    void merge() {
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

        assertEquals(ListUtil.of(-1, 0, 1), result);
    }

}