/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.pattern;

import java.util.Objects;
import java.util.SortedMap;
import java.util.StringJoiner;

public class FinishState<E> implements EventState<SortedMap<Long, E>, FinishState<E>> {

    private SortedMap<Long, E> events;

    public FinishState() {
    }

    public FinishState(SortedMap<Long, E> events) {
        this.events = events;
    }

    public static <E> FinishState<E> of(SortedMap<Long, E> events) {
        return new FinishState<>(events);
    }

    public E first() {
        return events.get(events.firstKey());
    }

    public E last() {
        return events.get(events.lastKey());
    }

    public Long firstKey() {
        return events.firstKey();
    }

    public Long lastKey() {
        return events.lastKey();
    }

    @Override
    public FinishState<E> merge(FinishState<E> that) {
        if (that == null) {
            return this;
        }
        this.events.putAll(that.events);
        return this;
    }

    @Override
    public FinishState<E> fastClone() {
        return new FinishState<>(events);
    }

    @Override
    public SortedMap<Long, E> value() {
        return events;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FinishState<?> that = (FinishState<?>) o;
        return Objects.equals(events, that.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(events);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FinishState.class.getSimpleName() + "[", "]").add("events=" + events)
            .toString();
    }
}
