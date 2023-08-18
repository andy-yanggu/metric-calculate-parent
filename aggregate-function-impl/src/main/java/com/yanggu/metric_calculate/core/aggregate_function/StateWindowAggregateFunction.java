package com.yanggu.metric_calculate.core.aggregate_function;

import lombok.Data;
import org.dromara.hutool.core.lang.mutable.MutableEntry;

import java.util.Objects;

/**
 * 状态窗口
 * <p>K相同时进行累加, 不相同时重新累加</p>
 */
@Data
public class StateWindowAggregateFunction<K, IN, ACC, OUT> implements AggregateFunction<MutableEntry<K, IN>, MutableEntry<K, ACC>, MutableEntry<K, OUT>> {

    private AggregateFunction<IN, ACC, OUT> aggregateFunction;

    @Override
    public MutableEntry<K, ACC> createAccumulator() {
        return new MutableEntry<>(null, aggregateFunction.createAccumulator());
    }

    @Override
    public MutableEntry<K, ACC> add(MutableEntry<K, IN> input,
                                                        MutableEntry<K, ACC> accumulator) {
        K oldStatus = accumulator.getKey();
        K newStatus = input.getKey();

        ACC acc = accumulator.getValue();
        if (oldStatus == null) {
            accumulator.setKey(newStatus);
            acc = aggregateFunction.add(input.getValue(), acc);
            //如果状态不相等
        } else if (!Objects.equals(newStatus, oldStatus)) {
            accumulator.setKey(newStatus);
            ACC newAccumulator = aggregateFunction.createAccumulator();
            acc = aggregateFunction.add(input.getValue(), newAccumulator);
        } else {
            //状态相等
            acc = aggregateFunction.add(input.getValue(), acc);
        }
        accumulator.setValue(acc);
        return accumulator;
    }

    @Override
    public MutableEntry<K, OUT> getResult(MutableEntry<K, ACC> accumulator) {
        return new MutableEntry<>(accumulator.getKey(), aggregateFunction.getResult(accumulator.getValue()));
    }

    @Override
    public MutableEntry<K, ACC> merge(MutableEntry<K, ACC> thisAccumulator,
                                                          MutableEntry<K, ACC> thatAccumulator) {
        return null;
    }

}
