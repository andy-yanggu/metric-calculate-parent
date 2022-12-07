package com.yanggu.metric_calculate.core.unit;

import com.yanggu.metric_calculate.core.value.Cloneable;

public interface MergedUnit<U extends MergedUnit<U> & Cloneable<U>> extends Unit<U> {
    @Override
    U fastClone();

    U merge(U that);

}
