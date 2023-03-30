package com.yanggu.metric_calculate.core2.aggregate_function;

import cn.hutool.core.lang.mutable.MutablePair;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import lombok.Data;

import java.util.Objects;

/**
 * 状态窗口
 * <p>MultiFieldDistinctKey相同时进行累加, 不相同时重新累加</p>
 */
@Data
public class StateWindowAggregateFunction<IN, ACC, OUT> implements AggregateFunction<MutablePair<MultiFieldDistinctKey, IN>, MutablePair<MultiFieldDistinctKey, ACC>, MutablePair<MultiFieldDistinctKey, OUT>> {

    private AggregateFunction<IN, ACC, OUT> aggregateFunction;

    @Override
    public MutablePair<MultiFieldDistinctKey, ACC> createAccumulator() {
        return new MutablePair<>(null, aggregateFunction.createAccumulator());
    }

    @Override
    public MutablePair<MultiFieldDistinctKey, ACC> add(MutablePair<MultiFieldDistinctKey, IN> input,
                                                       MutablePair<MultiFieldDistinctKey, ACC> accumulator) {
        MultiFieldDistinctKey oldStatus = accumulator.getKey();
        MultiFieldDistinctKey newStatus = input.getKey();

        ACC acc = accumulator.getValue();
        if (oldStatus == null) {
            accumulator.setKey(newStatus);
            acc = aggregateFunction.add(input.getValue(), acc);
            //如果状态不相等
        } else if (!Objects.equals(newStatus, oldStatus)) {
            accumulator.setKey(newStatus);
            ACC accumulator1 = aggregateFunction.createAccumulator();
            acc = aggregateFunction.add(input.getValue(), accumulator1);
        } else {
            //状态相等
            acc = aggregateFunction.add(input.getValue(), acc);
        }
        accumulator.setValue(acc);
        return accumulator;
    }

    @Override
    public MutablePair<MultiFieldDistinctKey, OUT> getResult(MutablePair<MultiFieldDistinctKey, ACC> accumulator) {
        return new MutablePair<>(accumulator.getKey(), aggregateFunction.getResult(accumulator.getValue()));
    }

}
