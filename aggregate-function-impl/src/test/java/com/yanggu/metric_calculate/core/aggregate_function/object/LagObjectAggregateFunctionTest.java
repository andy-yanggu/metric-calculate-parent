package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

class LagObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(LagObjectAggregateFunction.class, "LAGOBJECT");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(LagObjectAggregateFunction.class, 0, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(LagObjectAggregateFunction.class);
    }

}