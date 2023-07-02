package com.yanggu.metric_calculate.core2.aggregate_function;


/**
 * 定义输入数据、中间状态累计数据、输出数据
 * <p>该函数定义了如何进行增量计算，是一个无状态函数</p>
 * <p>需要空参构造方法，用于反射生成</p>
 * <p>聚合函数的相关参数，会在反射调用空参构造方法生成聚合函数对象后，反射给聚合函数的参数赋值</p>
 *
 * @param <IN> 输入的数据
 * @param <ACC> 中间累加数据 需要有空参构造方法，可以进行序列化和反序列化
 * @param <OUT> 输出的数据
 */
public interface AggregateFunction<IN, ACC, OUT> {

    /**
     * 初始化方法, 默认空代码, 实现类可以重写该方法
     * <p>给反射生成聚合函数，且给聚合函数的参数赋值完成后，会调用init方法</p>
     */
    default void init() {
    }

    /**
     * Creates a new accumulator, starting a new aggregate.
     *
     * <p>The new accumulator is typically meaningless unless a value is added via {@link
     * #add(IN, ACC)}.
     *
     * <p>The accumulator is the state of a running aggregation. When a program has multiple
     * aggregates in progress (such as per key and window), the state (per key and window) is the
     * size of the accumulator.
     *
     * @return A new accumulator, corresponding to an empty aggregate.
     */
    ACC createAccumulator();

    /**
     * Adds the given input into the given accumulator, returning the new accumulator input.
     *
     * <p>For efficiency, the input accumulator may be modified and returned.
     *
     * @param input The input to add
     * @param accumulator The accumulator to add the input to
     * @return The accumulator with the updated state
     */
    ACC add(IN input, ACC accumulator);

    /**
     * Gets the result of the aggregation from the accumulator.
     *
     * @param accumulator The accumulator of the aggregation
     * @return The final aggregation result.
     */
    OUT getResult(ACC accumulator);

    /**
     * Merges two accumulators, returning an accumulator with the merged state.
     *
     * <p>This function may reuse any of the given accumulators as the target for the merge and
     * return that. The assumption is that the given accumulators will not be used any more after
     * having been passed to this function.
     *
     * @param thisAccumulator An accumulator to merge
     * @param thatAccumulator Another accumulator to merge
     * @return The accumulator with the merged state
     */
    ACC merge(ACC thisAccumulator, ACC thatAccumulator);

}