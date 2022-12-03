package com.yanggu.metric_calculate.core.unit.pattern;

import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

public interface EventState<T, E extends EventState<T, E>> extends MergedUnit<E>, Value<T> {
}
