package com.yanggu.metric_calculate.core.value;

import java.util.Objects;

public class Cloneable2Wrapper<T> implements Cloneable2<Cloneable2Wrapper<T>>, Value<T> {

    private T wrapped;

    public Cloneable2Wrapper() {
    }

    private Cloneable2Wrapper(T wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Cloneable2Wrapper<T> fastClone() {
        return wrap(wrapped);
    }

    @Override
    public T value() {
        return wrapped;
    }

    /**
     * Wrap object with cloneable.
     */
    public static <W> Cloneable2Wrapper<W> wrap(W wrapped) {
        if (wrapped instanceof Cloneable2) {
            return new Cloneable2Wrapper(((Cloneable2) wrapped).fastClone());
        }
        return new Cloneable2Wrapper<>(wrapped);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cloneable2Wrapper<?> that = (Cloneable2Wrapper<?>) o;
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
