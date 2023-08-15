package com.yanggu.metric_calculate.core.aggregate_function;

import com.yanggu.metric_calculate.core.field_process.multi_field_distinct.MultiFieldDistinctKey;
import lombok.Data;
import org.dromara.hutool.core.lang.mutable.MutableEntry;

import java.util.Objects;

/**
 * 状态窗口
 * <p>MultiFieldDistinctKey相同时进行累加, 不相同时重新累加</p>
 */
@Data
public class StateWindowAggregateFunction<IN, ACC, OUT> implements AggregateFunction<MutableEntry<MultiFieldDistinctKey, IN>, MutableEntry<MultiFieldDistinctKey, ACC>, MutableEntry<MultiFieldDistinctKey, OUT>> {

    private AggregateFunction<IN, ACC, OUT> aggregateFunction;

    @Override
    public MutableEntry<MultiFieldDistinctKey, ACC> createAccumulator() {
        return new MutableEntry<>(null, aggregateFunction.createAccumulator());
    }

    @Override
    public MutableEntry<MultiFieldDistinctKey, ACC> add(MutableEntry<MultiFieldDistinctKey, IN> input,
                                                        MutableEntry<MultiFieldDistinctKey, ACC> accumulator) {
        MultiFieldDistinctKey oldStatus = accumulator.getKey();
        MultiFieldDistinctKey newStatus = input.getKey();

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
    public MutableEntry<MultiFieldDistinctKey, OUT> getResult(MutableEntry<MultiFieldDistinctKey, ACC> accumulator) {
        return new MutableEntry<>(accumulator.getKey(), aggregateFunction.getResult(accumulator.getValue()));
    }

    @Override
    public MutableEntry<MultiFieldDistinctKey, ACC> merge(MutableEntry<MultiFieldDistinctKey, ACC> thisAccumulator,
                                                          MutableEntry<MultiFieldDistinctKey, ACC> thatAccumulator) {
        return null;
    }

}
