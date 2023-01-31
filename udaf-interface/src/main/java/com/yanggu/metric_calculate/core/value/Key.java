package com.yanggu.metric_calculate.core.value;

import java.util.Objects;

public class Key<K extends Comparable<K>> implements Comparable<Key<K>>, Cloneable2<Key<K>> {

    private K data;

    private Cloneable2<Cloneable2Wrapper<K>> cloneable2Key;

    public Key() {
    }

    public Key(K data) {
        this.data = data;
        this.cloneable2Key = Cloneable2Wrapper.wrap(data);
    }

    @Override
    public Key<K> fastClone() {
        return new Key<>(cloneable2Key.fastClone().value());
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
        return Objects.hash(data, cloneable2Key);
    }

    @Override
    public String toString() {
        return data.toString();
    }

}


