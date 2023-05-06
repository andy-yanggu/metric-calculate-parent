package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 对象列表单元测试类
 */
public class ListObjectAggregateFunctionTest {

    @Test
    public void testCreateAccumulator() {
        AggregateFunction<String, List<String>, List<String>> function = new ListObjectAggregateFunction<>();
        List<String> accumulator = function.createAccumulator();
        assertEquals(0, accumulator.size());
    }

    @Test
    public void testAdd() {
        AggregateFunction<String, List<String>, List<String>> function = new ListObjectAggregateFunction<>();
        List<String> accumulator = new ArrayList<>();

        accumulator = function.add("a", accumulator);
        assertEquals(Collections.singletonList("a"), accumulator);

        accumulator = function.add("b", accumulator);
        assertEquals(Arrays.asList("a", "b"), accumulator);

        accumulator = function.add("c", accumulator);
        assertEquals(Arrays.asList("a", "b", "c"), accumulator);

        accumulator = function.add("d", accumulator);
        assertEquals(Arrays.asList("a", "b", "c", "d"), accumulator); // 元素数量未达到上限，可以添加d
    }

    @Test
    public void testGetResult() {
        AggregateFunction<String, List<String>, List<String>> function = new ListObjectAggregateFunction<>();
        List<String> accumulator = new ArrayList<>();
        accumulator = function.add("a", accumulator);
        accumulator = function.add("b", accumulator);
        accumulator = function.add("c", accumulator);
        List<String> result = function.getResult(accumulator);
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testMerge() {
        ListObjectAggregateFunction<String> function = new ListObjectAggregateFunction<>();

        List<String> accumulator1 = new ArrayList<>();
        accumulator1 = function.add("a", accumulator1);
        accumulator1 = function.add("b", accumulator1);
        accumulator1 = function.add("c", accumulator1);

        List<String> accumulator2 = new ArrayList<>();
        accumulator2 = function.add("d", accumulator2);
        accumulator2 = function.add("e", accumulator2);
        accumulator2 = function.add("f", accumulator2);

        List<String> result = function.merge(accumulator1, accumulator2);
        assertEquals(Arrays.asList("a", "b", "c", "d", "e", "f"), result);

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
        assertEquals(Arrays.asList("a", "b", "c", "d"), result); // 因为限制了最多4个元素，所以只有后4个元素能够保留
    }

}