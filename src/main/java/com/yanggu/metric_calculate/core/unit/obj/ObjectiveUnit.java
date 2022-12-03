package com.yanggu.metric_calculate.core.unit.obj;

import com.yanggu.metric_calculate.core.unit.MergedUnit;

public interface ObjectiveUnit<T, U extends ObjectiveUnit<T, U>> extends MergedUnit<U> {

    U value(T object);

}
