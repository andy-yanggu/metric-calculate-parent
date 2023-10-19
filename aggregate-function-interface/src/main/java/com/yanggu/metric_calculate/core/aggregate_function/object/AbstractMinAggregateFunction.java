package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import lombok.Data;
import org.dromara.hutool.core.lang.mutable.MutableObj;

import java.util.Comparator;

/**
 * 最小聚合函数抽象类
 * <p>子类需要设置比较器的逻辑</p>
 * <p>子类需要重写{@link AggregateFunction#getResult(Object)}方法</p>
 *
 * @param <IN> 输入数据类型
 * @param <OUT> 输出数据类型
 */
@Data
public abstract class AbstractMinAggregateFunction<IN, OUT> implements AggregateFunction<IN, MutableObj<IN>, OUT> {

    /**
     * 比较器必须是升序的逻辑
     */
    private Comparator<IN> comparator;

    @Override
    public MutableObj<IN> createAccumulator() {
        return new MutableObj<>();
    }

    @Override
    public MutableObj<IN> add(IN input, MutableObj<IN> accumulator) {
        IN oldValue = accumulator.get();
        //如果old为空
        if (oldValue == null || comparator.compare(input, oldValue) < 0) {
            accumulator.set(input);
        }
        return accumulator;
    }

    @Override
    public MutableObj<IN> merge(MutableObj<IN> thisAccumulator, MutableObj<IN> thatAccumulator) {
        IN thisValue = thisAccumulator.get();
        IN thatValue = thatAccumulator.get();
        if (thisValue == null && thatValue != null) {
            return thatAccumulator;
        } else if (thisValue == null/* && thatValue == null*/) {
            return thisAccumulator;
        } else if (/*thisValue != null && */thatValue == null) {
            return thisAccumulator;
        } else if (comparator.compare(thisValue, thatValue) < 0) {
            return thisAccumulator;
        } else {
            return thatAccumulator;
        }
    }

}
