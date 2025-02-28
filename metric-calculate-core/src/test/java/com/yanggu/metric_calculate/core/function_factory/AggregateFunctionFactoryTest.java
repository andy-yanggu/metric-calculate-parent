package com.yanggu.metric_calculate.core.function_factory;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactoryBase.AGGREGATE_FUNCTION_FACTORY;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("聚合函数工厂单元测试类")
class AggregateFunctionFactoryTest {

    @Test
    @Disabled("可能会报错")
    void test1() {
        AggregateFunction<Integer, Double, Double> aggregateFunction = AGGREGATE_FUNCTION_FACTORY.getAggregateFunction("TEST_SUM");
        AggregateFunctionFactory.initAggregateFunction(aggregateFunction, null);
        Double accumulator = aggregateFunction.createAccumulator();
        accumulator = aggregateFunction.add(1, accumulator);
        accumulator = aggregateFunction.add(2, accumulator);
        Double result = aggregateFunction.getResult(accumulator);
        assertEquals(3.0D, result, 0.0D);
    }

    @Test
    void test2() {
        AggregateFunction<Integer, Double, Double> aggregateFunction = AGGREGATE_FUNCTION_FACTORY.getAggregateFunction("SUM");
        AggregateFunctionFactory.initAggregateFunction(aggregateFunction, null);
        Double accumulator = aggregateFunction.createAccumulator();
        accumulator = aggregateFunction.add(1, accumulator);
        accumulator = aggregateFunction.add(2, accumulator);
        Double result = aggregateFunction.getResult(accumulator);
        assertEquals(3.0D, result, 0.0D);
    }

}
