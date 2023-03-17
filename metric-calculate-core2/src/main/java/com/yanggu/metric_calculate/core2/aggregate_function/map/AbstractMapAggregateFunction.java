package com.yanggu.metric_calculate.core2.aggregate_function.map;


import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import lombok.Data;
import reactor.util.function.Tuple2;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class AbstractMapAggregateFunction<IN, ACC, OUT>
        implements AggregateFunction<Tuple2<MultiFieldDistinctKey, IN>, Map<MultiFieldDistinctKey, ACC>, OUT> {

    private BaseUdafParam valueUdafParam;

    private AggregateFunctionFactory aggregateFunctionFactory;

    protected AggregateFunction<IN, ACC, OUT> valueAggregateFunction;

    @Override
    public void init() {
        AggregateFunction<IN, ACC, OUT> tempValueAggregateFunction
                = aggregateFunctionFactory.getAggregateFunction(valueUdafParam.getAggregateType());
        AggregateFunctionFactory.initAggregateFunction(tempValueAggregateFunction, valueUdafParam.getParam());
        this.valueAggregateFunction = tempValueAggregateFunction;
    }

    @Override
    public Map<MultiFieldDistinctKey, ACC> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public Map<MultiFieldDistinctKey, ACC> add(Tuple2<MultiFieldDistinctKey, IN> value,
                                               Map<MultiFieldDistinctKey, ACC> accumulator) {
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
