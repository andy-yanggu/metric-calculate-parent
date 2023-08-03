package com.yanggu.metric_calculate.core2.aggregate_function.mix;


import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Mix;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基本混合型，可以对计算后的数据进行二次处理
 *
 * @param <OUT>
 */
@Mix
@Data
@MergeType("BASEMIX")
@EqualsAndHashCode(callSuper=false)
public class BaseMixAggregateFunction<OUT> extends AbstractMixAggregateFunction<OUT> {
}
