package com.yanggu.metric_calculate.core.value;

import java.util.Objects;

public class Key<K extends Comparable<K>> implements Comparable<Key<K>>, Cloneable2<Key<K>> {

    private K key;
    private Cloneable2<Cloneable2Wrapper<K>> cloneable2Key;

    public Key() {
    }

    public Key(K key) {
        this.key = key;
        cloneable2Key = Cloneable2Wrapper.wrap(key);
    }

    public K key() {
        return key;
    }

    public Key<K> key(K key) {
        setKey(key);
        return this;
    }

    private void setKey(K key) {
        this.key = key;
        cloneable2Key = Cloneable2Wrapper.wrap(key);
    }

    public Cloneable2<Cloneable2Wrapper<K>> cloneableKey() {
        return cloneable2Key;
    }

    public Key<K> cloneableKey(Cloneable2<Cloneable2Wrapper<K>> cloneable2Key) {
        this.cloneable2Key = cloneable2Key;
        return this;
    }

    @Override
    public Key<K> fastClone() {
        return new Key(cloneable2Key.fastClone().value());
    }

    @Override
    public int compareTo(Key<K> o) {
        return key.compareTo(o.key);
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
        return Objects.equals(key, key1.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, cloneable2Key);
    }


    @Override
    public String toString() {
        return key.toString();
    }
}


