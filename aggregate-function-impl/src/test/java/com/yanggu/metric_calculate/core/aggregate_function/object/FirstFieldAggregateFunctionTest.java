package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

/**
 * 占位字段单元测试类
 */
class FirstFieldAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(FirstFieldAggregateFunction.class, "FIRSTFIELD");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(FirstFieldAggregateFunction.class, 0, 1);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(FirstFieldAggregateFunction.class);
    }

}