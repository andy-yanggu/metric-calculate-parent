package com.yanggu.metric_calculate.core2.field_process.aggregate;


import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.unit.AggregateFunction;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.List;

@Data
public class AggregateProcessor<T, IN, ACC, OUT> {

    private AggregateFunction<IN, ACC, OUT> aggregateFunction;

    private FieldProcessor<T, IN> fieldProcessor;

    @SneakyThrows
    public ACC exec(ACC oldAcc, T input) {
        if (oldAcc == null) {
            oldAcc = aggregateFunction.createAccumulator();
        }
        oldAcc = aggregateFunction.add(fieldProcessor.process(input), oldAcc);
        return oldAcc;
    }

    public OUT getMergeResult(List<ACC> accList) {
        ACC accumulator = aggregateFunction.createAccumulator();
        accumulator = accList.stream().reduce(accumulator, (acc, acc2) -> aggregateFunction.merge(acc, acc2));
        return aggregateFunction.getResult(accumulator);
    }

}
