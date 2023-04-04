package com.yanggu.metric_calculate.core2.table;


import cn.hutool.core.lang.mutable.MutablePair;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import lombok.Data;

/**
 * 状态窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class StatusWindowTable<IN, ACC, OUT> implements Table<MutablePair<MultiFieldDistinctKey, IN>, OUT> {

    private AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    @Override
    public void put(Long timestamp, MutablePair<MultiFieldDistinctKey, IN> in) {

    }

    @Override
    public OUT query() {
        return null;
    }

}
