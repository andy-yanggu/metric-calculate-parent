package com.yanggu.metric_calculate.core2.field_process.aggregate;


import com.yanggu.metric_calculate.core2.unit.AggregateFunction;
import lombok.Data;

import java.util.Collection;

@Data
public class AggregateProcessor<IN, ACC, OUT> {

    private AggregateFunction<IN, ACC, OUT> aggregateFunction;

    public ACC exec(ACC oldAcc, IN in) {
        if (oldAcc == null) {
            oldAcc = aggregateFunction.createAccumulator();
        }
        oldAcc = aggregateFunction.add(in, oldAcc);
        return oldAcc;
    }

    public OUT getMergeResult(Collection<ACC> accList) {
        ACC accumulator = aggregateFunction.createAccumulator();
        accumulator = accList.stream().reduce(accumulator, (acc, acc2) -> aggregateFunction.merge(acc, acc2));
        return aggregateFunction.getResult(accumulator);
    }

}
