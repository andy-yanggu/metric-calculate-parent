package com.yanggu.metric_calculate.core2.table;


import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;

public abstract class Table<IN, ACC, OUT> {

    protected AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    protected TimeBaselineDimension timeBaselineDimension;

    public void setAggregateFieldProcessor(AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor) {
        this.aggregateFieldProcessor = aggregateFieldProcessor;
    }

    public void setTimeBaselineDimension(TimeBaselineDimension timeBaselineDimension) {
        this.timeBaselineDimension = timeBaselineDimension;
    }

    /**
     * 放入度量值和时间戳进行累加
     *
     * @param timestamp
     * @param in
     */
    public abstract void put(Long timestamp, IN in);

    /**
     * 根据时间段进行查询
     *
     * @param from
     * @param fromInclusive
     * @param to
     * @param toInclusive
     * @return
     */
    public abstract OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive);

}
