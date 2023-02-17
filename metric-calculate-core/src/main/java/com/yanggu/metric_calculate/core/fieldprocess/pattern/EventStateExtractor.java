package com.yanggu.metric_calculate.core.fieldprocess.pattern;

import com.yanggu.metric_calculate.core.fieldprocess.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;

public class EventStateExtractor<T> implements AggregateFieldProcessor<T, MatchState<T>> {

    @Override
    public MatchState<T> process(T event) {
        return new MatchState<>(event);
    }

    @Override
    public String getAggregateType() {
        return null;
    }

    @Override
    public Class<? extends MergedUnit<?>> getMergeUnitClazz() {
        return null;
    }

}