package com.yanggu.metric_calculate.core2.aggregate_function.map;

import com.yanggu.metric_calculate.core2.aggregate_function.numeric.SumAggregateFunction;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * SortValueMapAggregateFunction单元测试类
 */
public class SortValueMapAggregateFunctionTest {

    /**
     * 默认升序取Top5
     */
    @Test
    public void testGetResult() {
        Map<String, Double> accumulator = new HashMap<>();
        accumulator.put("key1", 1.0D);
        accumulator.put("key2", 2.0D);
        accumulator.put("key3", 3.0D);
        accumulator.put("key4", 4.0D);
        accumulator.put("key5", 5.0D);
        accumulator.put("key6", 6.0D);
        accumulator.put("key7", 7.0D);
        accumulator.put("key8", 8.0D);
        accumulator.put("key9", 9.0D);
        accumulator.put("key10", 10.0D);

        SortValueMapAggregateFunction<String, Integer, Double, Double> sortValueMapAggregateFunction = new SortValueMapAggregateFunction<>();

        sortValueMapAggregateFunction.setValueAggregateFunction(new SumAggregateFunction<>());
        sortValueMapAggregateFunction.setLimit(5);
        Map<String, Double> actualResult = sortValueMapAggregateFunction.getResult(accumulator);
        Map<String, Double> expectedResult = new LinkedHashMap<>();
        expectedResult.put("key1", 1.0D);
        expectedResult.put("key2", 2.0D);
        expectedResult.put("key3", 3.0D);
        expectedResult.put("key4", 4.0D);
        expectedResult.put("key5", 5.0D);
        assertEquals(expectedResult, actualResult);
    }

    /**
     * 降序取Top5
     */
    @Test
    public void testGetResult_Negative() {
        Map<String, Double> accumulator = new HashMap<>();
        accumulator.put("key1", 1.0D);
        accumulator.put("key2", 2.0D);
        accumulator.put("key3", 3.0D);
        accumulator.put("key4", 4.0D);
        accumulator.put("key5", 5.0D);
        accumulator.put("key6", 6.0D);
        accumulator.put("key7", 7.0D);
        accumulator.put("key8", 8.0D);
        accumulator.put("key9", 9.0D);
        accumulator.put("key10", 10.0D);

        SortValueMapAggregateFunction<String, Integer, Double, Double> sortValueMapAggregateFunction = new SortValueMapAggregateFunction<>();

        sortValueMapAggregateFunction.setValueAggregateFunction(new SumAggregateFunction<>());
        sortValueMapAggregateFunction.setAsc(false);
        sortValueMapAggregateFunction.setLimit(5);
        Map<String, Double> actualResult = sortValueMapAggregateFunction.getResult(accumulator);
        Map<String, Double> expectedResult = new LinkedHashMap<>();
        expectedResult.put("key10", 10.0D);
        expectedResult.put("key9", 9.0D);
        expectedResult.put("key8", 8.0D);
        expectedResult.put("key7", 7.0D);
        expectedResult.put("key6", 6.0D);
        assertEquals(expectedResult, actualResult);
    }

}