package com.yanggu.metric_calculate.core2.aggregate_function.map;


import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import lombok.Data;
import reactor.util.function.Tuple2;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class AbstractMapAggregateFunction<IN, ACC, OUT>
        implements AggregateFunction<Tuple2<Object, IN>, Map<Object, ACC>, OUT> {

    protected AggregateFunction<IN, ACC, OUT> valueAggregateFunction;

    @Override
    public Map<Object, ACC> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public Map<Object, ACC> add(Tuple2<Object, IN> value, Map<Object, ACC> accumulator) {
        Object key = value.getT1();
        IN newValue = value.getT2();

        ACC acc = accumulator.get(key);
        if (acc == null) {
            acc = valueAggregateFunction.createAccumulator();
        }
        acc = valueAggregateFunction.add(newValue, acc);
        accumulator.put(key, acc);
        return accumulator;
    }

    @Override
    public Map<Object, ACC> merge(Map<Object, ACC> thisAccumulator, Map<Object, ACC> thatAccumulator) {
        thatAccumulator.forEach((tempKey, tempAcc) -> {
            ACC acc = thisAccumulator.get(tempKey);
            if (acc == null) {
                acc = valueAggregateFunction.createAccumulator();
            }
            acc = valueAggregateFunction.merge(acc, tempAcc);
            thisAccumulator.put(tempKey, acc);
        });
        return thisAccumulator;
    }

}
