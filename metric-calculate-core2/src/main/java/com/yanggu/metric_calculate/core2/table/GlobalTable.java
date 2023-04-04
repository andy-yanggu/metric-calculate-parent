package com.yanggu.metric_calculate.core2.table;


import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import lombok.Data;

@Data
public class GlobalTable<IN, ACC, OUT> implements Table<IN, ACC, OUT> {

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
