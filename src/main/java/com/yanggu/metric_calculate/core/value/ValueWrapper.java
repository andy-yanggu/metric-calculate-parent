package com.yanggu.metric_calculate.core.value;

public class ValueWrapper<V> implements Value<V> {

    private V wrapped;

    private ValueWrapper(V wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public V value() {
        return wrapped;
    }

    /**
     * Wrap object with cloneable.
     */
    public static <W> ValueWrapper<W> wrap(W wrapped) {
        return new ValueWrapper<>(wrapped);
    }


}
