package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.pojo.acc.BoundedPriorityQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 有序列表抽象类单元测试类
 */
class AbstractSortedListAggregateFunctionTest {

    private TestSortedListAggregateFunction<Integer> sortedListObjectAggregateFunction;

    @BeforeEach
    void init() {
        sortedListObjectAggregateFunction = new TestSortedListAggregateFunction<>();
    }

    @Test
    void createAccumulator() {
        BoundedPriorityQueue<Integer> accumulator = sortedListObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
        assertEquals(10, accumulator.getCapacity());
    }

    @Test
    void testAdd1() {
        //设置为3个
        sortedListObjectAggregateFunction.setLimit(3);
        BoundedPriorityQueue<Integer> accumulator = sortedListObjectAggregateFunction.createAccumulator();

        sortedListObjectAggregateFunction.add(1, accumulator);
        List<Integer> integers = accumulator.toList();
        assertEquals(List.of(1), integers);

        sortedListObjectAggregateFunction.add(3, accumulator);
        integers = accumulator.toList();
        assertEquals(List.of(1, 3), integers);

        sortedListObjectAggregateFunction.add(2, accumulator);
        integers = accumulator.toList();
        assertEquals(List.of(1, 2, 3), integers);

        sortedListObjectAggregateFunction.add(4, accumulator);
        integers = accumulator.toList();
        assertEquals(List.of(1, 2, 3), integers);

        sortedListObjectAggregateFunction.add(1, accumulator);
        integers = accumulator.toList();
        assertEquals(List.of(1, 1, 2), integers);
    }

    @Test
    void getResult() {
        BoundedPriorityQueue<Integer> accumulator = sortedListObjectAggregateFunction.createAccumulator();

        sortedListObjectAggregateFunction.add(1, accumulator);

        List<Integer> result = sortedListObjectAggregateFunction.getResult(accumulator);
        assertEquals(List.of(1), result);
    }

    @Test
    void testMerge() {
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

        BoundedPriorityQueue<Integer> merge = sortedListObjectAggregateFunction.merge(accumulator1, accumulator2);
        List<Integer> result = sortedListObjectAggregateFunction.getResult(merge);

        assertEquals(List.of(-1, 0, 1), result);
    }

}

class TestSortedListAggregateFunction<IN extends Comparable<IN>> extends AbstractSortedListAggregateFunction<IN, IN> {

    @Override
    public BoundedPriorityQueue<IN> createAccumulator() {
        return new BoundedPriorityQueue<>(getLimit());
    }

    @Override
    public IN inToOut(IN in) {
        return in;
    }

}
