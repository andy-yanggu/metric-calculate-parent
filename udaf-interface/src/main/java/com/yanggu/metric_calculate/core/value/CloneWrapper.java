package com.yanggu.metric_calculate.core.value;

import java.util.Objects;

public class CloneWrapper<T> implements Clone<CloneWrapper<T>>, Value<T> {

    private T wrapped;

    public CloneWrapper() {
    }

    public CloneWrapper(T wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public CloneWrapper<T> fastClone() {
        return wrap(wrapped);
    }

    @Override
    public T value() {
        return wrapped;
    }

    /**
     * Wrap object with cloneable.
     */
    public static <W> CloneWrapper<W> wrap(W wrapped) {
        if (wrapped instanceof Clone) {
            return new CloneWrapper(((Clone<?>) wrapped).fastClone());
        }
        return new CloneWrapper<>(wrapped);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CloneWrapper<?> that = (CloneWrapper<?>) o;
        return Objects.equals(wrapped, that.wrapped);
    }

    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }


    @Override
    public String toString() {
        return wrapped == null ? "null" : wrapped.toString();
    }

}
