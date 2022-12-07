package com.yanggu.metric_calculate.core.unit;


import com.yanggu.metric_calculate.core.value.TimeReferable;

public interface TimedUnit<U extends UnlimitedMergedUnit<U>> extends Unit<U>, TimeReferable {
}