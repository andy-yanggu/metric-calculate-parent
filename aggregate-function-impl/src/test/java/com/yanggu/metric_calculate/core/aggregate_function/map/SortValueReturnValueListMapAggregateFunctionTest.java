package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.aggregate_function.map.SortValueReturnKeyListMapAggregateFunctionTest.create;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SortValueReturnValueListMapAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(SortValueReturnValueListMapAggregateFunction.class, "SORTVALUERETURNVALUEMAP");
    }

    @Test
    void testMapType() {
        AggregateFunctionTestBase.testMapType(SortValueReturnValueListMapAggregateFunction.class);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(SortValueReturnValueListMapAggregateFunction.class);
    }

    @Test
    void testGetResult_Positive() {
        // 创建一个包含键值对的 HashMap
        Map<MultiFieldData, BigDecimal> accumulator = new HashMap<>();
        accumulator.put(create("key1"), BigDecimal.valueOf(5.0D));
        accumulator.put(create("key2"), BigDecimal.valueOf(10.0));
        accumulator.put(create("key3"), BigDecimal.valueOf(2.0D));
        // 创建一个 SortValueReturnValueListMapAggregateFunction 对象
        SortValueReturnValueListMapAggregateFunction<Integer, BigDecimal, BigDecimal> sortMapAggFunc =
                new SortValueReturnValueListMapAggregateFunction<>();
        sortMapAggFunc.setValueAggregateFunction(new SumAggregateFunction<>());
        sortMapAggFunc.setLimit(2);
        sortMapAggFunc.setAsc(false);
        // 创建预期的整数类型的列表
        List<BigDecimal> expectedList = new ArrayList<>();
        expectedList.add(BigDecimal.valueOf(10.0D));
        expectedList.add(BigDecimal.valueOf(5.0D));
        // 断言 SortValueReturnValueListMapAggregateFunction.getResult() 返回的值和预期的值一致
        assertEquals(expectedList, sortMapAggFunc.getResult(accumulator));
    }

    @Test
    void testGetResult_WithSameValues_Positive() {
        // 创建一个包含键值对的 HashMap
        Map<MultiFieldData, BigDecimal> accumulator = new HashMap<>();
        accumulator.put(create("key1"), BigDecimal.valueOf(5.0D));
        accumulator.put(create("key2"), BigDecimal.valueOf(10.0));
        accumulator.put(create("key3"), BigDecimal.valueOf(2.0D));
        accumulator.put(create("key4"), BigDecimal.valueOf(5.0D));
        // 创建一个 SortValueReturnValueListMapAggregateFunction 对象
        SortValueReturnValueListMapAggregateFunction<Integer, BigDecimal, BigDecimal> sortMapAggFunc =
                new SortValueReturnValueListMapAggregateFunction<>();
        sortMapAggFunc.setValueAggregateFunction(new SumAggregateFunction<>());
        sortMapAggFunc.setLimit(3);
        sortMapAggFunc.setAsc(true);
        // 创建预期的整数类型的列表
        List<BigDecimal> expectedList = new ArrayList<>();
        expectedList.add(BigDecimal.valueOf(2.0D));
        expectedList.add(BigDecimal.valueOf(5.0D));
        expectedList.add(BigDecimal.valueOf(5.0D));
        // 断言 SortValueReturnValueListMapAggregateFunction.getResult() 返回的值和预期的值一致
        assertEquals(expectedList, sortMapAggFunc.getResult(accumulator));
    }

}