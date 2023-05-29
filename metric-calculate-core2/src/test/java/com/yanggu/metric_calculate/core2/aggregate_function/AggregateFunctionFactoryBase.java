package com.yanggu.metric_calculate.core2.aggregate_function;


public class AggregateFunctionFactoryBase {

    public static AggregateFunctionFactory aggregateFunctionFactory;

    static {
        AggregateFunctionFactory tempAggregateFunctionFactory = new AggregateFunctionFactory();
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

}
