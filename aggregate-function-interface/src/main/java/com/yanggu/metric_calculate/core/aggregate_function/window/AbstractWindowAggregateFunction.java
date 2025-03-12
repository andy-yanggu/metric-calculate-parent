package com.yanggu.metric_calculate.core.aggregate_function.window;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import lombok.Data;

/**
 * 窗口类聚合函数抽象类
 */
@Data
public abstract class AbstractWindowAggregateFunction<IN1, ACC1, OUT1, IN2, ACC2, OUT2> implements AggregateFunction<IN1, ACC1, OUT1> {

    protected AggregateFunction<IN2, ACC2, OUT2> aggregateFunction;

}
