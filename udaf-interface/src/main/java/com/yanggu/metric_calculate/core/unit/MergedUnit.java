package com.yanggu.metric_calculate.core.unit;

import com.yanggu.metric_calculate.core.value.Cloneable2;

public interface MergedUnit<U extends MergedUnit<U> & Cloneable2<U>> extends Cloneable2<U> {

    U merge(U that);

    @Override
    U fastClone();

}
