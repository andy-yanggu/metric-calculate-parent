package com.yanggu.metric_calculate.core.unit;

import com.yanggu.metric_calculate.core.value.Cloneable;

public interface Unit<U extends Unit<U>> extends Cloneable<U> {
    @Override
    U fastClone();
}
