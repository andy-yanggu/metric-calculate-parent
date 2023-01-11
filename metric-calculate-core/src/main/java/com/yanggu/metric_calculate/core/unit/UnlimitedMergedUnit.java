package com.yanggu.metric_calculate.core.unit;


public interface UnlimitedMergedUnit<M extends UnlimitedMergedUnit<M>> extends MergedUnit<M> {
    M unlimitedMerge(M that);
}