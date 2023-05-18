package com.yanggu.metric_calculate.core2.aggregate_function.mix;


import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.type.seq.ArraySequence;
import com.googlecode.aviator.runtime.type.seq.CharSeqSequence;
import com.googlecode.aviator.runtime.type.seq.IterableSequence;
import com.googlecode.aviator.runtime.type.seq.MapSequence;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
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
            Class<?> clazz = result.getClass();
            //如果是数组
            if (clazz.isArray()) {
                env.put(tempKey, new ArraySequence(result));
                //如果是Map
            } else if (result instanceof Map) {
                env.put(tempKey, new MapSequence(((Map<?, ?>) result)));
                //如果是可遍历的(List、Set)
            } else if (result instanceof Iterable) {
                env.put(tempKey, new IterableSequence((Iterable<Object>) result));
                //如果是字符串
            } else if (result instanceof String) {
                env.put(tempKey, new CharSeqSequence((CharSequence) result));
            } else {
                //除此之外的都是标量, 直接放入原始数据即可
                env.put(tempKey, result);
            }
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
