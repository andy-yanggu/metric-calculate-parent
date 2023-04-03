package com.yanggu.metric_calculate.core2.table;


import cn.hutool.core.lang.mutable.MutablePair;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;

public class StatusWindowTable<IN, ACC, OUT> implements Table<MutablePair<MultiFieldDistinctKey, IN>, ACC, OUT> {

    private AggregateFunction<IN, ACC, OUT> aggregateFunction;

    private MutablePair<MultiFieldDistinctKey, ACC> mutablePair = new MutablePair<>(null, aggregateFunction.createAccumulator());

    @Override
    public void put(Long timestamp, MutablePair<MultiFieldDistinctKey, IN> in) {

    }

    @Override
    public OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return null;
    }

}
