package com.yanggu.metric_calculate.core2.aggregate_function.mix;


import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class AbstractMixAggregateFunction<OUT>
        implements AggregateFunction<Map<String, Object>, Map<String, Object>, OUT> {

    private Map<String, AggregateFunction<Object, Object, Object>> mixAggregateFunctionMap;

    private Expression expression;

    @Override
    public Map<String, Object> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> add(Map<String, Object> input, Map<String, Object> accumulator) {
        input.forEach((tempKey, tempValue) -> {
            AggregateFunction<Object, Object, Object> aggregateFunction = mixAggregateFunctionMap.get(tempKey);
            Object add = aggregateFunction.add(tempValue, accumulator.getOrDefault(tempKey, aggregateFunction.createAccumulator()));
            accumulator.put(tempKey, add);
        });
        return accumulator;
    }

    @Override
    public OUT getResult(Map<String, Object> accumulator) {
        return (OUT) expression.execute(accumulator);
    }

    @Override
    public Map<String, Object> merge(Map<String, Object> thisAccumulator,
                                     Map<String, Object> thatAccumulator) {
        thatAccumulator.forEach((tempKey, tempValue) -> {
            AggregateFunction<Object, Object, Object> aggregateFunction = mixAggregateFunctionMap.get(tempKey);
            Object merge = aggregateFunction.merge(thisAccumulator.getOrDefault(tempKey, aggregateFunction.createAccumulator()), tempValue);
            thisAccumulator.put(tempKey, merge);
        });
        return thisAccumulator;
    }

}
