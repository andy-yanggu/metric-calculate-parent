package com.yanggu.metric_calculate.core.aggregate_function.mix;


import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 混合型聚合函数
 *
 * @param <OUT>
 */
@Data
public abstract class AbstractMixAggregateFunction<OUT>
        implements AggregateFunction<Map<String, Object>, Map<String, Object>, OUT> {

    protected Map<String, AggregateFunction> mixAggregateFunctionMap;

    protected Expression expression;

    @Override
    public Map<String, Object> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> add(Map<String, Object> input, Map<String, Object> accumulator) {
        input.forEach((tempKey, tempValue) -> {
            AggregateFunction<Object, Object, Object> aggregateFunction = mixAggregateFunctionMap.get(tempKey);
            Object oldAcc = accumulator.getOrDefault(tempKey, aggregateFunction.createAccumulator());
            Object newAcc = aggregateFunction.add(tempValue, oldAcc);
            accumulator.put(tempKey, newAcc);
        });
        return accumulator;
    }

    @Override
    public OUT getResult(Map<String, Object> accumulator) {
        Map<String, Object> env = new HashMap<>();
        accumulator.forEach((tempKey, tempAcc) -> {
            AggregateFunction<Object, Object, Object> tempAggFunction = mixAggregateFunctionMap.get(tempKey);
            Object result = tempAggFunction.getResult(tempAcc);
            if (result == null) {
                return;
            }
            env.put(tempKey, result);
        });
        return (OUT) expression.execute(env);
    }

    @Override
    public Map<String, Object> merge(Map<String, Object> thisAccumulator,
                                     Map<String, Object> thatAccumulator) {
        thatAccumulator.forEach((tempKey, thatAcc) -> {
            AggregateFunction<Object, Object, Object> aggregateFunction = mixAggregateFunctionMap.get(tempKey);
            Object thisAcc = thisAccumulator.getOrDefault(tempKey, aggregateFunction.createAccumulator());
            Object merge = aggregateFunction.merge(thisAcc, thatAcc);
            thisAccumulator.put(tempKey, merge);
        });
        return thisAccumulator;
    }

}
