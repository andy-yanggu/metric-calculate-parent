package com.yanggu.metric_calculate.core.unit;

import com.yanggu.metric_calculate.core.value.Cloneable;

public interface MergedUnit<U extends MergedUnit<U> & Cloneable<U>> extends Cloneable<U> {

    U merge(U that);

}
