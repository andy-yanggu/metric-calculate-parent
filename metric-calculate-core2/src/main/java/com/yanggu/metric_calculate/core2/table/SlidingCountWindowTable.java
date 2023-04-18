package com.yanggu.metric_calculate.core2.table;

import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动计数窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class SlidingCountWindowTable<IN, ACC, OUT> implements Table<IN, OUT> {

    private Integer limit;

    private List<IN> inList = new ArrayList<>();

    private AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    @Override
    public void put(Long timestamp, IN in) {
        inList.add(in);
        while (inList.size() > limit) {
            inList.remove(0);
        }
    }

    @Override
    public OUT query() {
        return aggregateFieldProcessor.getOutFromInList(inList);
    }

}
