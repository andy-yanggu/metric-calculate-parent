/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.pattern;

import com.yanggu.metriccalculate.fieldprocess.FieldExtractProcessor;

public class EventStateExtractor<E> implements FieldExtractProcessor<E, EventState> {
    @Override
    public EventState process(E event) {
        return new MatchState(event);
    }
}
