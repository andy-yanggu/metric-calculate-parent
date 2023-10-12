package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 去重字段列表单元测试类
 */
class DistinctListFieldAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(DistinctListFieldAggregateFunction.class, "DISTINCTLISTFIELD");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(DistinctListFieldAggregateFunction.class, 1, 1);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(DistinctListFieldAggregateFunction.class);
    }

    @Test
    void testGetResult() {
        var distinctListFieldAggregateFunction = new DistinctListFieldAggregateFunction<Integer>();
        var keyValue = new KeyValue<MultiFieldDistinctKey, Integer>(null, 1);
        var set = Set.of(keyValue);
        List<Integer> result = distinctListFieldAggregateFunction.getResult(set);
        assertEquals(List.of(1), result);
    }

}
