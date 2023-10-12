package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

/**
 * 占位对象单元测试类
 */
class FirstObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(FirstObjectAggregateFunction.class, "FIRSTOBJECT");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(FirstObjectAggregateFunction.class, 0, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(FirstObjectAggregateFunction.class);
    }

}