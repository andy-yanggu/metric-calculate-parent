package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

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
        var acc = new HashSet<KeyValue<MultiFieldData, JSONObject>>();
        JSONObject data1 = new JSONObject();
        acc.add(new KeyValue<>(new MultiFieldData(List.of()), data1));
        List<JSONObject> result = distinctListObjectAggregateFunction.getResult(acc);
        assertEquals(List.of(data1), result);
    }

}