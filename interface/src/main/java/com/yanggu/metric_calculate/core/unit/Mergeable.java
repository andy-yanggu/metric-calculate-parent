package com.yanggu.metric_calculate.core.unit;

public interface Mergeable<M extends Mergeable> {
    M merge(M that);
}
