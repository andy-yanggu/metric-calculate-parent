package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

/**
 * 取代对象单元测试类
 */
class LastObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(LastObjectAggregateFunction.class, "LASTOBJECT");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(LastObjectAggregateFunction.class, 0, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(LastObjectAggregateFunction.class);
    }

}