package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 基本映射类型单元测试类
 */
class BaseMapAggregateFunctionTest {

    private BaseMapAggregateFunction<Double, BigDecimal, BigDecimal> basemap;

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
        MultiFieldData mapKey = new MultiFieldData(List.of("张三"));
        basemap.add(new Pair<>(mapKey, 100.0D), accumulator);
        Map<List<Object>, BigDecimal> result = basemap.getResult(accumulator);
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100.0D), result.get(List.of("张三")));
    }

}