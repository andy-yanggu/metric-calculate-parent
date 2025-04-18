package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DistinctListAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(DistinctListAggregateFunction.class, "DISTINCTLIST");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(DistinctListAggregateFunction.class, 1, 0);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(DistinctListAggregateFunction.class);
    }

    @Test
    void testGetResult() {
        DistinctListAggregateFunction distinctListAggregateFunction = new DistinctListAggregateFunction();
        Set<MultiFieldData> acc = new HashSet<>();
        acc.add(new MultiFieldData(List.of(1)));
        List<List<Object>> lists = distinctListAggregateFunction.getResult(acc);
        Assertions.assertEquals(List.of(List.of(1)), lists);
    }

}
