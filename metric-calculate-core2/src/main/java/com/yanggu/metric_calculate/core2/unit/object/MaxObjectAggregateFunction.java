package com.yanggu.metric_calculate.core2.unit.object;

import com.yanggu.metric_calculate.core2.KeyValue;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core2.unit.AggregateFunction;

/**
 * @version V1.0
 * @author: YangGu
 * @date: 2023/3/15 21:16
 * @description:
 */
public class MaxObjectAggregateFunction<T> implements AggregateFunction<KeyValue<MultiFieldOrderCompareKey, T>, KeyValue<MultiFieldOrderCompareKey, T>, T> {

    @Override
    public KeyValue<MultiFieldOrderCompareKey, T> createAccumulator() {
        return new KeyValue<>(MultiFieldOrderCompareKey.MIN, null);
    }

    @Override
    public KeyValue<MultiFieldOrderCompareKey, T> add(KeyValue<MultiFieldOrderCompareKey, T> value, KeyValue<MultiFieldOrderCompareKey, T> accumulator) {
        //accumulator.compareTo(value)
        return null;
    }

    @Override
    public T getResult(KeyValue<MultiFieldOrderCompareKey, T> accumulator) {
        return null;
    }

    @Override
    public KeyValue<MultiFieldOrderCompareKey, T> merge(KeyValue<MultiFieldOrderCompareKey, T> a, KeyValue<MultiFieldOrderCompareKey, T> b) {
        return null;
    }
}
