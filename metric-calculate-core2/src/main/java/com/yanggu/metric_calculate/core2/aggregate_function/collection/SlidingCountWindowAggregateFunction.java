package com.yanggu.metric_calculate.core2.aggregate_function.collection;


import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动计数窗口函数
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
@Collective
@MergeType("SLIDINGCOUNTWINDOW")
public class SlidingCountWindowAggregateFunction<IN, ACC, OUT> implements AggregateFunction<IN, List<IN>, OUT> {

    private Integer limit = 10;

    private AggregateFunction<IN, ACC, OUT> aggregateFunction;

    @Override
    public List<IN> createAccumulator() {
        return new ArrayList<>();
    }

    @Override
    public List<IN> add(IN input, List<IN> accumulator) {
        accumulator.add(input);
        while (accumulator.size() > limit) {
            accumulator.remove(0);
        }
        return accumulator;
    }

    @Override
    public OUT getResult(List<IN> accumulator) {
        ACC acc = aggregateFunction.createAccumulator();
        for (IN in : accumulator) {
            acc = aggregateFunction.add(in, acc);
        }
        return aggregateFunction.getResult(acc);
    }

    @Override
    public List<IN> merge(List<IN> thisAccumulator, List<IN> thatAccumulator) {
        thisAccumulator.addAll(thatAccumulator);
        while (thisAccumulator.size() > limit) {
            thisAccumulator.remove(0);
        }
        return thisAccumulator;
    }

}
