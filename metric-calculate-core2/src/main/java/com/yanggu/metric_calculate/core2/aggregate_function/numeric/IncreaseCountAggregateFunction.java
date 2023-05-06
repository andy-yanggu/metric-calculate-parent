package com.yanggu.metric_calculate.core2.aggregate_function.numeric;

import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Numerical;
import com.yanggu.metric_calculate.core2.pojo.agg_bean.Boundary;

/**
 * 递增次数
 */
@Numerical
@MergeType("INCREASECOUNT")
public class IncreaseCountAggregateFunction<T extends Number & Comparable<T>> implements AggregateFunction<T, Boundary<T>, Integer> {

    @Override
    public Boundary<T> createAccumulator() {
        return new Boundary<>();
    }

    @Override
    public Boundary<T> add(T input, Boundary<T> accumulator) {
        T head = accumulator.getHead();
        T tail = accumulator.getTail();
        if (head == null && tail == null) {
            accumulator.setHead(input);
            accumulator.setTail(input);
            accumulator.setValue(0);
        } else {
            if (input.compareTo(tail) > 0) {
                accumulator.setValue(accumulator.getValue() + 1);
            }
            accumulator.setTail(input);
        }
        return accumulator;
    }

    @Override
    public Integer getResult(Boundary<T> accumulator) {
        return accumulator.getValue();
    }

    @Override
    public Boundary<T> merge(Boundary<T> thisAccumulator, Boundary<T> thatAccumulator) {
        T thisHead = thisAccumulator.getHead();
        T thisTail = thisAccumulator.getTail();
        if (thisHead == null && thisTail == null) {
            thisAccumulator.setHead(thatAccumulator.getHead());
            thisAccumulator.setTail(thatAccumulator.getTail());
            thisAccumulator.setValue(thatAccumulator.getValue());
        } else {
            thisAccumulator.setTail(thatAccumulator.getTail());
            Integer value = thisAccumulator.getValue() + thatAccumulator.getValue();
            if (thisTail.compareTo(thatAccumulator.getHead()) < 0) {
                value++;
            }
            thisAccumulator.setValue(value);
        }
        return thisAccumulator;
    }

}
