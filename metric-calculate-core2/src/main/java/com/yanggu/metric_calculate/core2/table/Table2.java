package com.yanggu.metric_calculate.core2.table;


import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;

/**
 * 进行分桶的抽象类
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
public abstract class Table2<IN, ACC, OUT> implements Table<IN, ACC, OUT> {

    protected AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    protected TimeBaselineDimension timeBaselineDimension;

    public void setAggregateFieldProcessor(AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor) {
        this.aggregateFieldProcessor = aggregateFieldProcessor;
    }

    public void setTimeBaselineDimension(TimeBaselineDimension timeBaselineDimension) {
        this.timeBaselineDimension = timeBaselineDimension;
    }

}
