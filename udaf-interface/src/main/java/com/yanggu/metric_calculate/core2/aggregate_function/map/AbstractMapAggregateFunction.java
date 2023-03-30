package com.yanggu.metric_calculate.core2.aggregate_function.map;


import cn.hutool.core.lang.Pair;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 映射类型的基类
 *
 * @param <K> map的key
 * @param <V> map的value输入值
 * @param <ValueACC> map的value聚合值
 * @param <ValueOUT> map的value输出值
 * @param <OUT> 输出值
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
        K key = input.getKey();
        V newValue = input.getValue();

        ValueACC acc = accumulator.get(key);
        if (acc == null) {
            acc = valueAggregateFunction.createAccumulator();
        }
        acc = valueAggregateFunction.add(newValue, acc);
        accumulator.put(key, acc);
        return accumulator;
    }

    @Override
    public Map<K, ValueACC> merge(Map<K, ValueACC> thisAccumulator, Map<K, ValueACC> thatAccumulator) {
        thatAccumulator.forEach((tempKey, tempAcc) -> {
            ValueACC acc = thisAccumulator.get(tempKey);
            if (acc == null) {
                acc = valueAggregateFunction.createAccumulator();
            }
            acc = valueAggregateFunction.merge(acc, tempAcc);
            thisAccumulator.put(tempKey, acc);
        });
        return thisAccumulator;
    }

    @Override
    public OUT getResult(Map<K, ValueACC> accumulator) {
        Map<K, ValueOUT> map = new HashMap<>();
        accumulator.forEach((tempKey, tempValueAcc) -> map.put(tempKey, valueAggregateFunction.getResult(tempValueAcc)));
        return (OUT) map;
    }
    
}
