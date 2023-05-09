package com.yanggu.metric_calculate.core2.aggregate_function.map;

import com.yanggu.metric_calculate.core2.aggregate_function.numeric.SumAggregateFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SortValueReturnValueMapAggregateFunctionTest {

    @Test
    public void testGetResult_Positive() {
        // 创建一个包含键值对的 HashMap
        Map<String, Double> accumulator = new HashMap<>();
        accumulator.put("key1", 5.0D);
        accumulator.put("key2", 10.0D);
        accumulator.put("key3", 2.0D);
        // 创建一个 SortValueReturnValueMapAggregateFunction 对象
        SortValueReturnValueMapAggregateFunction<String, Integer, Double, Double> sortMapAggFunc =
                new SortValueReturnValueMapAggregateFunction<>();
        sortMapAggFunc.setValueAggregateFunction(new SumAggregateFunction<>());
        sortMapAggFunc.setLimit(2);
        sortMapAggFunc.setAsc(false);
        // 创建预期的整数类型的列表
        List<Double> expectedList = new ArrayList<>();
        expectedList.add(10.0D);
        expectedList.add(5.0D);
        // 断言 SortValueReturnValueMapAggregateFunction.getResult() 返回的值和预期的值一致
        assertEquals(expectedList, sortMapAggFunc.getResult(accumulator));
    }

    @Test
    public void testGetResult_WithSameValues_Positive() {
        // 创建一个包含键值对的 HashMap
        Map<String, Double> accumulator = new HashMap<>();
        accumulator.put("key1", 5.0D);
        accumulator.put("key2", 10.0D);
        accumulator.put("key3", 2.0D);
        accumulator.put("key4", 5.0D);
        // 创建一个 SortValueReturnValueMapAggregateFunction 对象
        SortValueReturnValueMapAggregateFunction<String, Integer, Double, Double> sortMapAggFunc =
                new SortValueReturnValueMapAggregateFunction<>();
        sortMapAggFunc.setValueAggregateFunction(new SumAggregateFunction<>());
        sortMapAggFunc.setLimit(3);
        sortMapAggFunc.setAsc(true);
        // 创建预期的整数类型的列表
        List<Double> expectedList = new ArrayList<>();
        expectedList.add(2.0D);
        expectedList.add(5.0D);
        expectedList.add(5.0D);
        // 断言 SortValueReturnValueMapAggregateFunction.getResult() 返回的值和预期的值一致
        assertEquals(expectedList, sortMapAggFunc.getResult(accumulator));
    }

}