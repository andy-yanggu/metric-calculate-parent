package com.yanggu.metric_calculate.core.unit;

import com.yanggu.metric_calculate.core.value.Cloneable;

public interface MergedUnit<U extends MergedUnit<U> & Cloneable<U>> extends Unit<U>, Mergeable<U> {

    @Override
    U fastClone();

    @Override
    U merge(U that);

}
