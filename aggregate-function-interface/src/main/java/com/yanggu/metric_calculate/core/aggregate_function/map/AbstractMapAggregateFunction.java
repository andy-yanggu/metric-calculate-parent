package com.yanggu.metric_calculate.core.aggregate_function.map;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import lombok.Data;
import org.dromara.hutool.core.lang.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * 映射类型的基类
 * <p>会对{@link AbstractMapAggregateFunction#valueAggregateFunction}进行赋值</p>
 * <p>子类可以根据需要重写{@link AggregateFunction#getResult(Object)}方法</p>
 * <p>IN的输入类型是定死的{@link Pair}</p>
 *
 * @param <K>        map的key
 * @param <V>        map的value输入值
 * @param <ValueACC> map的value聚合值
 * @param <ValueOUT> map的value输出值
 * @param <OUT>      输出值
 */
@Data
public abstract class AbstractMapAggregateFunction<K, V, ValueACC, ValueOUT, OUT>
        implements AggregateFunction<Pair<K, V>, Map<K, ValueACC>, OUT> {

    protected AggregateFunction<V, ValueACC, ValueOUT> valueAggregateFunction;

    @Override
    public Map<K, ValueACC> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public Map<K, ValueACC> add(Pair<K, V> input, Map<K, ValueACC> accumulator) {
        K key = input.getLeft();
        ValueACC oldAcc = accumulator.getOrDefault(key, valueAggregateFunction.createAccumulator());
        ValueACC newAcc = valueAggregateFunction.add(input.getRight(), oldAcc);
        accumulator.put(key, newAcc);
        return accumulator;
    }

    @Override
    public OUT getResult(Map<K, ValueACC> accumulator) {
        Map<K, ValueOUT> map = new HashMap<>();
        accumulator.forEach((k, acc) -> map.put(k, valueAggregateFunction.getResult(acc)));
        return (OUT) map;
    }

    @Override
    public Map<K, ValueACC> merge(Map<K, ValueACC> thisAccumulator, Map<K, ValueACC> thatAccumulator) {
        thatAccumulator.forEach((tempKey, thatAcc) -> {
            ValueACC thisAcc = thisAccumulator.getOrDefault(tempKey, valueAggregateFunction.createAccumulator());
            ValueACC newAcc = valueAggregateFunction.merge(thisAcc, thatAcc);
            thisAccumulator.put(tempKey, newAcc);
        });
        return thisAccumulator;
    }

}
