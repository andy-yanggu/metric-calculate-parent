package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.aggregate_function.map.SortValueReturnKeyListMapAggregateFunctionTest.create;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * SortValueReturnMapMapAggregateFunction单元测试类
 */
class SortValueReturnMapMapAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(SortValueReturnMapMapAggregateFunction.class, "SORTVALUEMAP");
    }

    @Test
    void testMapType() {
        AggregateFunctionTestBase.testMapType(SortValueReturnMapMapAggregateFunction.class);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(SortValueReturnMapMapAggregateFunction.class);
    }

    /**
     * 默认升序取Top5
     */
    @Test
    void testGetResult() {
        Map<MultiFieldData, BigDecimal> accumulator = new HashMap<>();
        accumulator.put(create("key1"), BigDecimal.valueOf(1.0D));
        accumulator.put(create("key2"), BigDecimal.valueOf(2.0D));
        accumulator.put(create("key3"), BigDecimal.valueOf(3.0D));
        accumulator.put(create("key4"), BigDecimal.valueOf(4.0D));
        accumulator.put(create("key5"), BigDecimal.valueOf(5.0D));
        accumulator.put(create("key6"), BigDecimal.valueOf(6.0D));
        accumulator.put(create("key7"), BigDecimal.valueOf(7.0D));
        accumulator.put(create("key8"), BigDecimal.valueOf(8.0D));
        accumulator.put(create("key9"), BigDecimal.valueOf(9.0D));
        accumulator.put(create("key10"), BigDecimal.valueOf(10.0D));

        SortValueReturnMapMapAggregateFunction<Integer, BigDecimal, BigDecimal> sortValueReturnMapMapAggregateFunction = new SortValueReturnMapMapAggregateFunction<>();

        sortValueReturnMapMapAggregateFunction.setValueAggregateFunction(new SumAggregateFunction<>());
        sortValueReturnMapMapAggregateFunction.setLimit(5);
        Map<List<Object>, BigDecimal> actualResult = sortValueReturnMapMapAggregateFunction.getResult(accumulator);
        Map<List<Object>, BigDecimal> expectedResult = new LinkedHashMap<>();
        expectedResult.put(List.of("key1"), BigDecimal.valueOf(1.0D));
        expectedResult.put(List.of("key2"), BigDecimal.valueOf(2.0D));
        expectedResult.put(List.of("key3"), BigDecimal.valueOf(3.0D));
        expectedResult.put(List.of("key4"), BigDecimal.valueOf(4.0D));
        expectedResult.put(List.of("key5"), BigDecimal.valueOf(5.0D));
        assertEquals(expectedResult, actualResult);
    }

    /**
     * 降序取Top5
     */
    @Test
    void testGetResult_Negative() {
        Map<MultiFieldData, BigDecimal> accumulator = new HashMap<>();
        accumulator.put(create("key1"), BigDecimal.valueOf(1.0D));
        accumulator.put(create("key2"), BigDecimal.valueOf(2.0D));
        accumulator.put(create("key3"), BigDecimal.valueOf(3.0D));
        accumulator.put(create("key4"), BigDecimal.valueOf(4.0D));
        accumulator.put(create("key5"), BigDecimal.valueOf(5.0D));
        accumulator.put(create("key6"), BigDecimal.valueOf(6.0D));
        accumulator.put(create("key7"), BigDecimal.valueOf(7.0D));
        accumulator.put(create("key8"), BigDecimal.valueOf(8.0D));
        accumulator.put(create("key9"), BigDecimal.valueOf(9.0D));
        accumulator.put(create("key10"), BigDecimal.valueOf(10.0D));

        SortValueReturnMapMapAggregateFunction<Integer, BigDecimal, BigDecimal> sortValueReturnMapMapAggregateFunction = new SortValueReturnMapMapAggregateFunction<>();

        sortValueReturnMapMapAggregateFunction.setValueAggregateFunction(new SumAggregateFunction<>());
        sortValueReturnMapMapAggregateFunction.setAsc(false);
        sortValueReturnMapMapAggregateFunction.setLimit(5);
        Map<List<Object>, BigDecimal> actualResult = sortValueReturnMapMapAggregateFunction.getResult(accumulator);
        Map<List<Object>, BigDecimal> expectedResult = new LinkedHashMap<>();
        expectedResult.put(List.of("key10"), BigDecimal.valueOf(10.0D));
        expectedResult.put(List.of("key9"), BigDecimal.valueOf(9.0D));
        expectedResult.put(List.of("key8"), BigDecimal.valueOf(8.0D));
        expectedResult.put(List.of("key7"), BigDecimal.valueOf(7.0D));
        expectedResult.put(List.of("key6"), BigDecimal.valueOf(6.0D));
        assertEquals(expectedResult, actualResult);
    }

}