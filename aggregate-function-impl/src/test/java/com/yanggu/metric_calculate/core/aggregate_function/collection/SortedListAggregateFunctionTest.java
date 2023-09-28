package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

/**
 * 有序列表单元测试类
 */
class SortedListAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(SortedListAggregateFunction.class, "SORTEDLIMITLIST");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(SortedListAggregateFunction.class, 2, 0);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(SortedListAggregateFunction.class);
    }

}