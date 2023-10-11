package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 对象列表单元测试类
 */
class ListObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(ListObjectAggregateFunction.class, "LISTOBJECT");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(ListObjectAggregateFunction.class, 0, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(ListObjectAggregateFunction.class);
    }

    @Test
    void testCreateAccumulator() {
        var function = new ListObjectAggregateFunction();
        var accumulator = function.createAccumulator();
        assertEquals(0, accumulator.size());
    }

    @Test
    void testAdd() {
        var function = new ListObjectAggregateFunction();
        List<JSONObject> accumulator = new ArrayList<>();

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
    void testGetResult() {
        AggregateFunction<JSONObject, List<JSONObject>, List<JSONObject>> function = new ListObjectAggregateFunction<>();
        List<JSONObject> accumulator = new ArrayList<>();
        accumulator = function.add("a", accumulator);
        accumulator = function.add("b", accumulator);
        accumulator = function.add("c", accumulator);
        List<JSONObject> result = function.getResult(accumulator);
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    void testMerge() {
        ListObjectAggregateFunction<JSONObject> function = new ListObjectAggregateFunction<>();

        List<JSONObject> accumulator1 = new ArrayList<>();
        accumulator1 = function.add("a", accumulator1);
        accumulator1 = function.add("b", accumulator1);
        accumulator1 = function.add("c", accumulator1);

        List<JSONObject> accumulator2 = new ArrayList<>();
        accumulator2 = function.add("d", accumulator2);
        accumulator2 = function.add("e", accumulator2);
        accumulator2 = function.add("f", accumulator2);

        List<JSONObject> result = function.merge(accumulator1, accumulator2);
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