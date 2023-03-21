package com.yanggu.metric_calculate.core2.aggregate_function.collection;


import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

import java.util.List;

@MergeType("LISTOBJECTCOUNTWINDOW")
@Collective
public class SlidingCountWindowAggregateFunction<IN, OUT> implements AggregateFunction<IN, List<IN>, OUT> {
    @Override
    public List<IN> createAccumulator() {
        return null;
    }

    @Override
    public List<IN> add(IN value, List<IN> accumulator) {
        return null;
    }

    @Override
    public OUT getResult(List<IN> accumulator) {
        return null;
    }

    @Override
    public List<IN> merge(List<IN> thisAccumulator, List<IN> thatAccumulator) {
        return null;
    }
}
