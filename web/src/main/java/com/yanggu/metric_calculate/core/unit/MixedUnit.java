package com.yanggu.metric_calculate.core.unit;

import com.yanggu.metric_calculate.core.value.Cloneable;

import java.util.Collection;

public interface MixedUnit<U extends MixedUnit<U> & Cloneable<U>> extends MergedUnit<U> {

    U mixMerge(U mixUnit);

    U mixMerge(MergedUnit unit);

    Collection<Class<? extends MergedUnit>> supportUnit();

}
