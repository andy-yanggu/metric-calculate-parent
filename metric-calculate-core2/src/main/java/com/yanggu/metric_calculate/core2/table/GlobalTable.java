package com.yanggu.metric_calculate.core2.table;


import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import lombok.Data;

/**
 * 全窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class GlobalTable<IN, ACC, OUT> implements Table<IN, OUT> {

    private AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    private ACC accumulator;

    @Override
    public void put(Long timestamp, IN in) {
        accumulator = aggregateFieldProcessor.add(accumulator, in);
    }

    @Override
    public OUT query() {
        return aggregateFieldProcessor.getOutFromAcc(accumulator);
    }

}
