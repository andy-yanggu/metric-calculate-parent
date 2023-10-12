package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

/**
 * 对象列表单元测试类
 */
class ListObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(ListObjectAggregateFunction.class, "LISTOBJECT");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(ListObjectAggregateFunction.class, 0, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(ListObjectAggregateFunction.class);
    }

}