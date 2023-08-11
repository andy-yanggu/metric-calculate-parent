package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.pojo.agg_bean.Measures;

/**
 * 方差
 */
public class VarpAggregateFunction<T extends Number> implements AggregateFunction<T, Measures, Double> {

    @Override
    public Measures createAccumulator() {
        return new Measures();
    }

    @Override
    public Measures add(T input, Measures accumulator) {
        Double sum = accumulator.getSum();
        Integer count = accumulator.getCount();
        Double avg = count == 0 ? 0.0D : sum / count;
        Double variance = accumulator.getVariance();

        sum = sum + input.doubleValue();
        accumulator.setSum(sum);
        count += 1;
        accumulator.setCount(count);

        //Double newVarp = ;
        return accumulator;
    }

    @Override
    public Double getResult(Measures accumulator) {
        return accumulator.getVariance();
    }

    @Override
    public Measures merge(Measures thisAccumulator, Measures thatAccumulator) {
        return null;
    }

}
