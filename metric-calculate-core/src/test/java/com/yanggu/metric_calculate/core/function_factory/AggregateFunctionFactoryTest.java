package com.yanggu.metric_calculate.core.function_factory;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.test.TestJarUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AggregateFunctionFactoryTest {

    public static AggregateFunctionFactory aggregateFunctionFactory;

    static {
        AggregateFunctionFactory tempAggregateFunctionFactory = new AggregateFunctionFactory(TestJarUtil.testJarPath());
        try {
            tempAggregateFunctionFactory.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        aggregateFunctionFactory = tempAggregateFunctionFactory;
    }

    public static AggregateFunctionFactory getAggregateFunctionFactory() {
        return aggregateFunctionFactory;
    }

    @Test
    @Disabled("可能会报错")
    void test1() {
        AggregateFunction<Integer, Double, Double> aggregateFunction = aggregateFunctionFactory.getAggregateFunction("TEST_SUM");
        AggregateFunctionFactory.initAggregateFunction(aggregateFunction, null);
        Double accumulator = aggregateFunction.createAccumulator();
        accumulator = aggregateFunction.add(1, accumulator);
        accumulator = aggregateFunction.add(2, accumulator);
        Double result = aggregateFunction.getResult(accumulator);
        assertEquals(3.0D, result, 0.0D);
    }

    @Test
    void test2() {
        AggregateFunction<Integer, Double, Double> aggregateFunction = aggregateFunctionFactory.getAggregateFunction("SUM");
        AggregateFunctionFactory.initAggregateFunction(aggregateFunction, null);
        Double accumulator = aggregateFunction.createAccumulator();
        accumulator = aggregateFunction.add(1, accumulator);
        accumulator = aggregateFunction.add(2, accumulator);
        Double result = aggregateFunction.getResult(accumulator);
        assertEquals(3.0D, result, 0.0D);
    }

}
