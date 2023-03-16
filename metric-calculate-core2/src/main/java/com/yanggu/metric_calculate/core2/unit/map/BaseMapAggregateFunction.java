package com.yanggu.metric_calculate.core2.unit.map;

import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.unit.AggregateFunction;
import reactor.util.function.Tuple2;

import java.util.HashMap;
import java.util.Map;


public class BaseMapAggregateFunction<IN, ACC, OUT> implements AggregateFunction<Tuple2<MultiFieldDistinctKey, IN>, Map<MultiFieldDistinctKey, ACC>, Map<MultiFieldDistinctKey, OUT>> {

    private AggregateFunction<IN, ACC, OUT> valueAggregateFunction;

    @Override
    public Map<MultiFieldDistinctKey, ACC> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public Map<MultiFieldDistinctKey, ACC> add(Tuple2<MultiFieldDistinctKey, IN> value, Map<MultiFieldDistinctKey, ACC> accumulator) {
        MultiFieldDistinctKey key = value.getT1();
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
    public Map<MultiFieldDistinctKey, OUT> getResult(Map<MultiFieldDistinctKey, ACC> accumulator) {
        Map<MultiFieldDistinctKey, OUT> returnMap = new HashMap<>();
        accumulator.forEach((tempKey, tempAcc) -> returnMap.put(tempKey, valueAggregateFunction.getResult(tempAcc)));
        return returnMap;
    }

    @Override
    public Map<MultiFieldDistinctKey, ACC> merge(Map<MultiFieldDistinctKey, ACC> thisAccumulator,
                                                 Map<MultiFieldDistinctKey, ACC> thatAccumulator) {
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
