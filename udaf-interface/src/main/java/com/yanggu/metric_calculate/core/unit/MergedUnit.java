package com.yanggu.metric_calculate.core.unit;

import com.yanggu.metric_calculate.core.value.Clone;

public interface MergedUnit<U extends MergedUnit<U> & Clone<U>> extends Clone<U> {

    U merge(U that);

    @Override
    U fastClone();

}
