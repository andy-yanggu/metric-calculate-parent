package com.yanggu.metric_calculate.core.unit.pattern;

import com.yanggu.metric_calculate.core.fieldprocess.FieldExtractProcessor;

public class EventStateExtractor<E> implements FieldExtractProcessor<E, EventState> {
    @Override
    public EventState process(E event) {
        return new MatchState(event);
    }
}
