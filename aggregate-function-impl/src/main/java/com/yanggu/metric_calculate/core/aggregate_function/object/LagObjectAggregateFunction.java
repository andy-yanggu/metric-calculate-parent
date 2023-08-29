package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import lombok.Data;

import java.util.LinkedList;

/**
 * 当前行的第前N条对象
 */
@Data
@Objective(keyStrategy = 0, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "LAGOBJECT", displayName = "当前行的第前N条对象")
public class LagObjectAggregateFunction<IN> implements AggregateFunction<IN, LinkedList<IN>, IN> {

    /**
     * 偏移量
     */
    private int offset = 1;

    /**
     * 没有值, 返回默认值
     */
    private IN defaultValue;

    @Override
    public LinkedList<IN> createAccumulator() {
        return new LinkedList<>();
    }

    @Override
    public LinkedList<IN> add(IN input, LinkedList<IN> accumulator) {
        accumulator.add(input);
        //删除offset之前的数据
        while (accumulator.size() > offset + 1) {
            accumulator.removeFirst();
        }
        return accumulator;
    }

    @Override
    public IN getResult(LinkedList<IN> accumulator) {
        //如果没有到达第N个, 直接返回默认值
        if (accumulator.size() < offset + 1) {
            return defaultValue;
            //如果刚好到达第N个, 返回头
        } else if (accumulator.size() == offset + 1) {
            return accumulator.getFirst();
        } else {
            //如果大于, 返回第accumulator.size() - offset - 1个
            return accumulator.get(accumulator.size() - offset - 1);
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
