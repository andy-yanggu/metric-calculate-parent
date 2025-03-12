package com.yanggu.metric_calculate.core.aggregate_function.window;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.lang.mutable.MutablePair;

import java.util.Objects;

/**
 * 状态窗口
 * <p>K相同时进行累加, 不相同时重新累加</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StateWindowAggregateFunction<K, IN, ACC, OUT> extends
        AbstractWindowAggregateFunction<MutablePair<K, IN>, MutablePair<K, ACC>, MutablePair<K, OUT>, IN, ACC, OUT> {

    @Override
    public MutablePair<K, ACC> createAccumulator() {
        return new MutablePair<>(null, aggregateFunction.createAccumulator());
    }

    @Override
    public MutablePair<K, ACC> add(MutablePair<K, IN> input,
                                   MutablePair<K, ACC> accumulator) {
        K oldStatus = accumulator.getLeft();
        K newStatus = input.getLeft();

        ACC acc = accumulator.getRight();
        if (oldStatus == null) {
            accumulator.setLeft(newStatus);
            acc = aggregateFunction.add(input.getRight(), acc);
            //如果状态不相等
        } else if (!Objects.equals(newStatus, oldStatus)) {
            accumulator.setLeft(newStatus);
            ACC newAccumulator = aggregateFunction.createAccumulator();
            acc = aggregateFunction.add(input.getRight(), newAccumulator);
        } else {
            //状态相等
            acc = aggregateFunction.add(input.getRight(), acc);
        }
        accumulator.setRight(acc);
        return accumulator;
    }

    @Override
    public MutablePair<K, OUT> getResult(MutablePair<K, ACC> accumulator) {
        return new MutablePair<>(accumulator.getLeft(), aggregateFunction.getResult(accumulator.getRight()));
    }

    @Override
    public MutablePair<K, ACC> merge(MutablePair<K, ACC> thisAccumulator,
                                     MutablePair<K, ACC> thatAccumulator) {
        K thisStatus = thisAccumulator.getLeft();
        K thatStatus = thatAccumulator.getLeft();

        //如果状态不同，无法合并
        if (!Objects.equals(thisStatus, thatStatus)) {
            throw new IllegalArgumentException("Cannot merge accumulators with different states");
        }

        //合并累加器
        ACC newAcc = aggregateFunction.merge(thisAccumulator.getRight(), thatAccumulator.getRight());
        return new MutablePair<>(thisStatus, newAcc);
    }

}
