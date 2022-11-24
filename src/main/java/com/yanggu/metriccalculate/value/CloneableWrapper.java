/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.value;

import java.util.Objects;
import com.yanggu.metriccalculate.value.Cloneable;

public class CloneableWrapper<T> implements Cloneable<CloneableWrapper<T>>, Value<T> {

    private T wrapped;

    public CloneableWrapper() {
    }

    private CloneableWrapper(T wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public CloneableWrapper<T> fastClone() {
        return wrap(wrapped);
    }

    @Override
    public T value() {
        return wrapped;
    }

    /**
     * Wrap object with cloneable.
     */
    public static <W> CloneableWrapper<W> wrap(W wrapped) {
        if (wrapped instanceof Cloneable) {
            return new CloneableWrapper(((Cloneable) wrapped).fastClone());
        }
        return new CloneableWrapper<>(wrapped);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CloneableWrapper<?> that = (CloneableWrapper<?>) o;
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
