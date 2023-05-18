package com.yanggu.metric_calculate.core2.aggregate_function.mix;


import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Mix;
import lombok.Data;

@Mix
@Data
@MergeType("BASEMIX")
public class BaseMixAggregateFunction<OUT> extends AbstractMixAggregateFunction<OUT> {
}
