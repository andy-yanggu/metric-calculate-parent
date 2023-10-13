//package com.yanggu.metric_calculate.core.aggregate_function.collection;
//
//
//import com.yanggu.metric_calculate.core.pojo.acc.BoundedPriorityQueue;
//import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
//import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;
//import org.dromara.hutool.core.collection.ListUtil;
//import org.dromara.hutool.core.reflect.FieldUtil;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * 有序列表抽象类单元测试类
// */
//class AbstractSortedListAggregateFunctionTest {
//
//    @Test
//    void createAccumulator() {
//        SortedListAggregateFunction2<Integer> sortedListObjectAggregateFunction = new SortedListAggregateFunction2<>();
//        var accumulator = sortedListObjectAggregateFunction.createAccumulator();
//        assertNotNull(accumulator);
//        assertTrue(accumulator.isEmpty());
//        assertEquals(10, FieldUtil.getFieldValue(accumulator, "capacity"));
//    }
//
//    @Test
//    void testAdd1() {
//        SortedListAggregateFunction2<Integer> sortedListObjectAggregateFunction = new SortedListAggregateFunction2<>();
//        //设置为3个
//        sortedListObjectAggregateFunction.setLimit(3);
//        var accumulator = sortedListObjectAggregateFunction.createAccumulator();
//
//        sortedListObjectAggregateFunction.add(1, accumulator);
//        List<Integer> integers = accumulator.toList();
//        assertEquals(ListUtil.of(1), integers);
//
//        sortedListObjectAggregateFunction.add(3, accumulator);
//        integers = accumulator.toList();
//        assertEquals(ListUtil.of(1, 3), integers);
//
//        sortedListObjectAggregateFunction.add(2, accumulator);
//        integers = accumulator.toList();
//        assertEquals(ListUtil.of(1, 2, 3), integers);
//
//        sortedListObjectAggregateFunction.add(4, accumulator);
//        integers = accumulator.toList();
//        assertEquals(ListUtil.of(1, 2, 3), integers);
//
//        sortedListObjectAggregateFunction.add(1, accumulator);
//        integers = accumulator.toList();
//        assertEquals(ListUtil.of(1, 1, 2), integers);
//    }
//
//    @Test
//    void getResult() {
//        SortedListAggregateFunction2<Integer> sortedListObjectAggregateFunction = new SortedListAggregateFunction2<>();
//        BoundedPriorityQueue<Integer> accumulator = sortedListObjectAggregateFunction.createAccumulator();
//
//        sortedListObjectAggregateFunction.add(1, accumulator);
//
//        List<Integer> result = sortedListObjectAggregateFunction.getResult(accumulator);
//        assertEquals(ListUtil.of(1), result);
//    }
//
//    @Test
//    void merge() {
//        SortedListAggregateFunction2<Integer> sortedListObjectAggregateFunction = new SortedListAggregateFunction2<>();
//        sortedListObjectAggregateFunction.setLimit(3);
//
//        BoundedPriorityQueue<Integer> accumulator1 = sortedListObjectAggregateFunction.createAccumulator();
//        BoundedPriorityQueue<Integer> accumulator2 = sortedListObjectAggregateFunction.createAccumulator();
//
//        sortedListObjectAggregateFunction.add(1, accumulator1);
//        sortedListObjectAggregateFunction.add(3, accumulator1);
//        sortedListObjectAggregateFunction.add(2, accumulator1);
//        sortedListObjectAggregateFunction.add(4, accumulator1);
//
//        sortedListObjectAggregateFunction.add(-1, accumulator2);
//        sortedListObjectAggregateFunction.add(0, accumulator2);
//        sortedListObjectAggregateFunction.add(1, accumulator2);
//        sortedListObjectAggregateFunction.add(2, accumulator2);
//
//        List<Integer> result = sortedListObjectAggregateFunction.getResult(sortedListObjectAggregateFunction.merge(accumulator1, accumulator2));
//
//        assertEquals(List.of(-1, 0, 1), result);
//    }
//
//}
//
//class SortedListAggregateFunction2<T extends Comparable<T>> extends AbstractSortedListAggregateFunction<T, T> {
//
//    @Override
//    public T inToOut(KeyValue<MultiFieldDistinctKey, T> in) {
//        return (T) in.getKey().getFieldList().get(0);
//    }
//
//    @Override
//    public BoundedPriorityQueue<KeyValue<MultiFieldDistinctKey, T>> createAccumulator() {
//        return new BoundedPriorityQueue<>(getLimit());
//    }
//
//}
