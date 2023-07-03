package com.yanggu.metric_calculate.core2.aggregate_function;


import com.yanggu.metric_calculate.core2.test.TestJarUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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
    @Ignore
    public void test1() {
        AggregateFunction<Integer, Double, Double> aggregateFunction = aggregateFunctionFactory.getAggregateFunction("TEST_SUM");
        AggregateFunctionFactory.initAggregateFunction(aggregateFunction, null);
        Double accumulator = aggregateFunction.createAccumulator();
        accumulator = aggregateFunction.add(1, accumulator);
        accumulator = aggregateFunction.add(2, accumulator);
        Double result = aggregateFunction.getResult(accumulator);
        Assert.assertEquals(3.0D, result, 0.0D);
    }

    @Test
    public void test2() {
        AggregateFunction<Integer, Double, Double> aggregateFunction = aggregateFunctionFactory.getAggregateFunction("SUM");
        AggregateFunctionFactory.initAggregateFunction(aggregateFunction, null);
        Double accumulator = aggregateFunction.createAccumulator();
        accumulator = aggregateFunction.add(1, accumulator);
        accumulator = aggregateFunction.add(2, accumulator);
        Double result = aggregateFunction.getResult(accumulator);
        Assert.assertEquals(3.0D, result, 0.0D);
    }

}
