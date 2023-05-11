package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import lombok.Data;

import java.util.LinkedList;

/**
 * 第前N条数据
 */
@Data
@MergeType("LAGOBJECT")
@Objective(useCompareField = false)
public class LagObjectAggregateFunction<IN> implements AggregateFunction<IN, LinkedList<IN>, IN> {

    private int offset = 1;

    private IN defaultValue = null;

    @Override
    public LinkedList<IN> createAccumulator() {
        return new LinkedList<>();
    }

    @Override
    public LinkedList<IN> add(IN input, LinkedList<IN> accumulator) {
        accumulator.add(input);
        while (accumulator.size() > offset + 1) {
            accumulator.removeFirst();
        }
        return accumulator;
    }

    @Override
    public IN getResult(LinkedList<IN> accumulator) {
        if (accumulator.size() < offset + 1) {
            return defaultValue;
        } else if (accumulator.size() == offset + 1) {
            return accumulator.getFirst();
        } else {
            throw new RuntimeException("too more elements: " + accumulator);
        }
    }

    @Override
    public LinkedList<IN> merge(LinkedList<IN> thisAccumulator, LinkedList<IN> thatAccumulator) {
        thisAccumulator.addAll(thatAccumulator);
        while (thisAccumulator.size() > offset + 1) {
            thisAccumulator.removeFirst();
        }
        return thisAccumulator;
    }

}
