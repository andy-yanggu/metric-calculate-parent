package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

/**
 * 取代字段单元测试类
 */
class LastFieldAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(LastFieldAggregateFunction.class, "LASTFIELD");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(LastFieldAggregateFunction.class, 0, 1);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(LastFieldAggregateFunction.class);
    }

}