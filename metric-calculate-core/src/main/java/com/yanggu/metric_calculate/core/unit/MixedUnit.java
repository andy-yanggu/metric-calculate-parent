package com.yanggu.metric_calculate.core.unit;

import java.util.Collection;
import com.yanggu.metric_calculate.core.value.Cloneable2;

public interface MixedUnit<U extends MixedUnit<U> & Cloneable2<U>> extends MergedUnit<U> {

    U mixMerge(U mixUnit);

    U mixMerge(MergedUnit unit);

    Collection<Class<? extends MergedUnit>> supportUnit();

}
