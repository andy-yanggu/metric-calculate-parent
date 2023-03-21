package com.yanggu.metric_calculate.core2.aggregate_function;


public class AggregateFunctionFactoryTest {

    public static AggregateFunctionFactory aggregateFunctionFactory;

    static {
        AggregateFunctionFactory tempAggregateFunctionFactory = new AggregateFunctionFactory();
        tempAggregateFunctionFactory.init();
        aggregateFunctionFactory = tempAggregateFunctionFactory;
    }

    public static AggregateFunctionFactory getAggregateFunctionFactory() {
        return aggregateFunctionFactory;
    }

}
