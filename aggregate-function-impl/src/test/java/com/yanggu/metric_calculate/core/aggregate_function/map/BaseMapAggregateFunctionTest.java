package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 基本映射类型单元测试类
 */
class BaseMapAggregateFunctionTest {

    private BaseMapAggregateFunction<Double, Double, Double> basemap;

    @BeforeEach
    void init() {
        this.basemap = new BaseMapAggregateFunction<>();
        SumAggregateFunction<Double> sumAggregateFunction = new SumAggregateFunction<>();
        this.basemap.setValueAggregateFunction(sumAggregateFunction);
    }

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(BaseMapAggregateFunction.class, "BASEMAP");
    }

    @Test
    void testMapType() {
        AggregateFunctionTestBase.testMapType(BaseMapAggregateFunction.class);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(BaseMapAggregateFunction.class);
    }

    @Test
    void getResult() {
        var accumulator = basemap.createAccumulator();
        MultiFieldDistinctKey mapKey = new MultiFieldDistinctKey(List.of("张三"));
        basemap.add(new AbstractMap.SimpleImmutableEntry<>(mapKey, 100.0D), accumulator);
        Map<List<Object>, Double> result = basemap.getResult(accumulator);
        assertNotNull(result);
        assertEquals(100.0D, result.get(List.of("张三")), 0.0D);
    }

}