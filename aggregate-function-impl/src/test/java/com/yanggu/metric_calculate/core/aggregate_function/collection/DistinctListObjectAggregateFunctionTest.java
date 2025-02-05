package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 去重对象列表单元测试类
 */
class DistinctListObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(DistinctListObjectAggregateFunction.class, "DISTINCTLISTOBJECT");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(DistinctListObjectAggregateFunction.class, 1, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(DistinctListObjectAggregateFunction.class);
    }

    @Test
    void testGetResult() {
        var distinctListObjectAggregateFunction = new DistinctListObjectAggregateFunction();
        var acc = new HashSet<Pair<MultiFieldData, Map<String, Object>>>();
        Map<String, Object> data1 = new HashMap<>();
        acc.add(new Pair<>(new MultiFieldData(List.of()), data1));
        List<Map<String, Object>> result = distinctListObjectAggregateFunction.getResult(acc);
        assertEquals(List.of(data1), result);
    }

}