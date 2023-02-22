package com.yanggu.metric_calculate.core.value;

import java.util.Objects;

public class Key<K extends Comparable<K>> implements Comparable<Key<K>>, Clone<Key<K>> {

    private K data;

    private Clone<CloneWrapper<K>> cloneKey;

    public Key() {
    }

    public Key(K data) {
        this.data = data;
        this.cloneKey = CloneWrapper.wrap(data);
    }

    @Override
    public Key<K> fastClone() {
        return new Key<>(cloneKey.fastClone().value());
    }

    @Override
    public int compareTo(Key<K> o) {
        return data.compareTo(o.data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Key<?> key1 = (Key<?>) o;
        return Objects.equals(data, key1.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, cloneKey);
    }

    @Override
    public String toString() {
        return data.toString();
    }

}


