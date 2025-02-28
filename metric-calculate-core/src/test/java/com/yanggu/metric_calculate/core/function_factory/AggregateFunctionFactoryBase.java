package com.yanggu.metric_calculate.core.function_factory;


import com.yanggu.metric_calculate.core.util.TestJarUtil;

public class AggregateFunctionFactoryBase {

    public static AggregateFunctionFactory AGGREGATE_FUNCTION_FACTORY;

    static {
        AggregateFunctionFactory tempAggregateFunctionFactory = new AggregateFunctionFactory(TestJarUtil.testJarPath());
        try {
            tempAggregateFunctionFactory.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        AGGREGATE_FUNCTION_FACTORY = tempAggregateFunctionFactory;
    }

}
