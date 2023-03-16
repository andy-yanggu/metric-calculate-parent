package com.yanggu.metric_calculate.core2.unit;


import com.yanggu.metric_calculate.core2.unit.numeric.SumAggregateFunction;

import java.util.HashMap;
import java.util.Map;

public class AggregateFunctionFactory {

    /**
     * 内置MergeUnit的包路径
     */
    public static final String SCAN_PACKAGE = "com.yanggu.metric_calculate.core2.unit";

    /**
     * 内置的MergeUnit
     */
    private static final Map<String, Class<? extends AggregateFunction>> BUILT_IN_UNIT_MAP = new HashMap<>();

    static {

    }

    public static <IN, ACC, OUT> AggregateFunction<IN, ACC, OUT> getAggregateFunction(String aggregate) {
        return new SumAggregateFunction();
    }


}
