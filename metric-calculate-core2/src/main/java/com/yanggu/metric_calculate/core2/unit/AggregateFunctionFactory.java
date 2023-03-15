package com.yanggu.metric_calculate.core2.unit;


import com.yanggu.metric_calculate.core2.unit.numeric.SumAggregateFunction;

public class AggregateFunctionFactory {

    public static <IN, ACC, OUT> AggregateFunction<IN, ACC, OUT> getAggregateFunction(String aggregate) {
        return new SumAggregateFunction();
    }


}
