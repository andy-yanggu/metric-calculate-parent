package com.yanggu.metric_calculate.core.aggregate_function.mix;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.junit.jupiter.api.Test;

class BaseMixAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(BaseMixAggregateFunction.class, "BASEMIX");
    }

    @Test
    void testMapType() {
        AggregateFunctionTestBase.testMix(BaseMixAggregateFunction.class);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(BaseMixAggregateFunction.class);
    }

}