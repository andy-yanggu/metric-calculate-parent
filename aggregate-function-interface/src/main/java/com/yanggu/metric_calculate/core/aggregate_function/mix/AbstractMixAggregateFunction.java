package com.yanggu.metric_calculate.core.aggregate_function.mix;


import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 混合型聚合函数抽象类
 * <p>子类可以根据需求重写相应方法</p>
 * <p>mixAggregateFunctionMap和expression都会进行赋值</p>
 *
 * @param <OUT> 输出的数据类型
 */
@Data
public abstract class AbstractMixAggregateFunction<OUT> implements AggregateFunction<Map<String, Object>, Map<String, Object>, OUT> {

    protected Map<String, AggregateFunction> mixAggregateFunctionMap;

    protected Expression expression;

    @Override
    public Map<String, Object> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> add(Map<String, Object> input, Map<String, Object> accumulator) {
        input.forEach((tempKey, tempValue) -> {
            AggregateFunction<Object, Object, Object> tempAggFunction = mixAggregateFunctionMap.get(tempKey);
            Object oldAcc = accumulator.getOrDefault(tempKey, tempAggFunction.createAccumulator());
            Object newAcc = tempAggFunction.add(tempValue, oldAcc);
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
            if (result != null) {
                env.put(tempKey, result);
            }
        });
        return (OUT) expression.execute(env);
    }

    @Override
    public Map<String, Object> merge(Map<String, Object> thisAccumulator,
                                     Map<String, Object> thatAccumulator) {
        thatAccumulator.forEach((tempKey, thatAcc) -> {
            AggregateFunction<Object, Object, Object> tempAggFunction = mixAggregateFunctionMap.get(tempKey);
            Object thisAcc = thisAccumulator.getOrDefault(tempKey, tempAggFunction.createAccumulator());
            Object merge = tempAggFunction.merge(thisAcc, thatAcc);
            thisAccumulator.put(tempKey, merge);
        });
        return thisAccumulator;
    }

}
