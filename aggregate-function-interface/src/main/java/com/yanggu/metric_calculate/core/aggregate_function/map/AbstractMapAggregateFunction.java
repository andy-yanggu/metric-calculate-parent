package com.yanggu.metric_calculate.core.aggregate_function.map;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import lombok.Data;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 映射类型的基类
 * <p>子类可以根据需要重写{@link AggregateFunction#getResult(Object)}方法</p>
 *
 * @param <K>        map的key
 * @param <V>        map的value输入值
 * @param <ValueACC> map的value聚合值
 * @param <ValueOUT> map的value输出值
 * @param <OUT>      输出值
 */
@Data
public abstract class AbstractMapAggregateFunction<K, V, ValueACC, ValueOUT, OUT>
        implements AggregateFunction<AbstractMap.SimpleImmutableEntry<K, V>, Map<K, ValueACC>, OUT> {

    protected AggregateFunction<V, ValueACC, ValueOUT> valueAggregateFunction;

    @Override
    public Map<K, ValueACC> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public Map<K, ValueACC> add(AbstractMap.SimpleImmutableEntry<K, V> input, Map<K, ValueACC> accumulator) {
        K key = input.getKey();

        ValueACC oldAcc = accumulator.getOrDefault(key, valueAggregateFunction.createAccumulator());
        ValueACC newAcc = valueAggregateFunction.add(input.getValue(), oldAcc);
        accumulator.put(key, newAcc);
        return accumulator;
    }

    @Override
    public OUT getResult(Map<K, ValueACC> accumulator) {
        Map<K, ValueOUT> map = new HashMap<>();
        accumulator.forEach((tempKey, tempValueAcc) -> {
            ValueOUT result = valueAggregateFunction.getResult(tempValueAcc);
            if (result != null) {
                map.put(tempKey, result);
            }
        });
        return (OUT) map;
    }

    @Override
    public Map<K, ValueACC> merge(Map<K, ValueACC> thisAccumulator, Map<K, ValueACC> thatAccumulator) {
        thatAccumulator.forEach((tempKey, thatAcc) -> {
            ValueACC thisAcc = thisAccumulator.getOrDefault(tempKey, valueAggregateFunction.createAccumulator());
            thisAcc = valueAggregateFunction.merge(thisAcc, thatAcc);
            thisAccumulator.put(tempKey, thisAcc);
        });
        return thisAccumulator;
    }

    /**
     * 根据value进行排序并进行截取
     *
     * @param accumulator
     * @param asc
     * @param limit
     * @return
     */
    protected Stream<AbstractMap.SimpleImmutableEntry<K, ValueOUT>> getCompareLimitStream(
                                                                            Map<K, ValueACC> accumulator,
                                                                            Boolean asc,
                                                                            Integer limit) {
        Comparator<AbstractMap.SimpleImmutableEntry<K, ValueOUT>> pairComparator = (o1, o2) -> {
            Comparable value1 = (Comparable) o1.getValue();
            Comparable value2 = (Comparable) o2.getValue();
            int compareTo = value1.compareTo(value2);
            if (Boolean.TRUE.equals(asc)) {
                return compareTo;
            } else {
                return -compareTo;
            }
        };
        return accumulator.entrySet().stream()
                .map(tempEntry -> new AbstractMap.SimpleImmutableEntry<>(tempEntry.getKey(), valueAggregateFunction.getResult(tempEntry.getValue())))
                .filter(tempEntry -> tempEntry.getValue() != null)
                .sorted(pairComparator)
                .limit(limit);
    }

}
