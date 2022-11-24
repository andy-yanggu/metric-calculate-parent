package com.yanggu.metriccalculate.unit.pattern;

import java.util.Objects;
import java.util.StringJoiner;
import com.yanggu.metriccalculate.value.Cloneable;

public class MatchState<E> implements EventState<E, MatchState<E>> {

    private E event;

    public MatchState() {
    }

    public MatchState(E event) {
        this.event = event;
    }

    public static <E> MatchState<E> of(E event) {
        return new MatchState<>(event);
    }

    @Override
    public MatchState<E> merge(MatchState<E> that) {
        if (that == null) {
            return this;
        }
        this.event = that.event;
        return this;
    }

    @Override
    public MatchState<E> fastClone() {
        return new MatchState<>(event instanceof Cloneable ? (E) ((Cloneable) event).fastClone() : event);
    }

    @Override
    public E value() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MatchState<?> that = (MatchState<?>) o;
        return Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MatchState.class.getSimpleName() + "[", "]")
            .add("event=" + event).toString();
    }
}
