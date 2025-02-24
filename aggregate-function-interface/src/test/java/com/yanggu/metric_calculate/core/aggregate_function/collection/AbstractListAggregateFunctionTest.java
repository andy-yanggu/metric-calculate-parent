package com.yanggu.metric_calculate.core.aggregate_function.collection;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 列表聚合函数抽象类单元测试类
 */
class AbstractListAggregateFunctionTest {

    private TestListAggregateFunction<String> function;

    @BeforeEach
    void init() {
        function = new TestListAggregateFunction<>();
    }

    @Test
    void testCreateAccumulator() {
        List<String> accumulator = function.createAccumulator();
        assertEquals(0, accumulator.size());
        assertInstanceOf(ArrayList.class, accumulator);
    }

    @Test
    void testAdd() {
        List<String> accumulator = new ArrayList<>();

        accumulator = function.add("a", accumulator);
        assertEquals(List.of("a"), accumulator);

        accumulator = function.add("b", accumulator);
        assertEquals(List.of("a", "b"), accumulator);

        accumulator = function.add("c", accumulator);
        assertEquals(List.of("a", "b", "c"), accumulator);

        accumulator = function.add("d", accumulator);
        //元素数量未达到上限，可以添加d
        assertEquals(List.of("a", "b", "c", "d"), accumulator);
    }

    @Test
    void testGetResult() {
        List<String> accumulator = new ArrayList<>();
        accumulator = function.add("a", accumulator);
        accumulator = function.add("b", accumulator);
        accumulator = function.add("c", accumulator);
        List<String> result = function.getResult(accumulator);
        assertEquals(List.of("a", "b", "c"), result);
    }

    @Test
    void testMerge() {
        List<String> accumulator1 = new ArrayList<>();
        accumulator1 = function.add("a", accumulator1);
        accumulator1 = function.add("b", accumulator1);
        accumulator1 = function.add("c", accumulator1);

        List<String> accumulator2 = new ArrayList<>();
        accumulator2 = function.add("d", accumulator2);
        accumulator2 = function.add("e", accumulator2);
        accumulator2 = function.add("f", accumulator2);

        List<String> result = function.merge(accumulator1, accumulator2);
        assertEquals(List.of("a", "b", "c", "d", "e", "f"), result);

        function.setLimit(4);
        accumulator1.clear();
        accumulator2.clear();

        accumulator1 = function.add("a", accumulator1);
        accumulator1 = function.add("b", accumulator1);
        accumulator1 = function.add("c", accumulator1);
        accumulator2 = function.add("d", accumulator2);
        accumulator2 = function.add("e", accumulator2);
        accumulator2 = function.add("f", accumulator2);
        result = function.merge(accumulator1, accumulator2);
        assertEquals(List.of("a", "b", "c", "d"), result); // 因为限制了最多4个元素，所以只有后4个元素能够保留
    }

}

class TestListAggregateFunction<IN> extends AbstractListAggregateFunction<IN> {
}
