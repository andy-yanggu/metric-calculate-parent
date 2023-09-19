package com.yanggu.metric_calculate.core.aggregate_function.mix;


import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Mix;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基本混合型，可以对计算后的数据进行二次处理
 *
 * @param <OUT>
 */
@Mix
@Data
@EqualsAndHashCode(callSuper = false)
@AggregateFunctionAnnotation(name = "BASEMIX", displayName = "基本混合型")
public class BaseMixAggregateFunction<OUT> extends AbstractMixAggregateFunction<OUT> {
}
