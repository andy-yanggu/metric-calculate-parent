package com.yanggu.metric_calculate.core2.unit.mix;


import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core2.unit.AggregateFunction;

import java.util.HashMap;
import java.util.Map;

public class BaseMixAggregateFunction implements AggregateFunction<Map<String, Object>, Map<String, Object>, Object> {

    private Map<String, AggregateFunction> mixAggregateFunctionMap;

    private Expression expression;
    
    @Override
    public Map<String, Object> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> add(Map<String, Object> value, Map<String, Object> accumulator) {
        accumulator.forEach((tempKey, tempValue) -> {

        });
        return accumulator;
    }

    @Override
    public Object getResult(Map<String, Object> accumulator) {
        return expression.execute(accumulator);
    }

    @Override
    public Map<String, Object> merge(Map<String, Object> thisAccumulator, Map<String, Object> thatAccumulator) {
        return null;
    }
    
}
