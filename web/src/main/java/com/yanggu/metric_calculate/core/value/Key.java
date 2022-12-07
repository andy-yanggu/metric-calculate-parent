package com.yanggu.metric_calculate.core.value;

import java.util.Objects;

public class Key<K extends Comparable<K>> implements Comparable<Key<K>>, Cloneable<Key<K>> {

    private K key;
    private Cloneable<CloneableWrapper<K>> cloneableKey;

    public Key() {
    }

    public Key(K key) {
        this.key = key;
        cloneableKey = CloneableWrapper.wrap(key);
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
        cloneableKey = CloneableWrapper.wrap(key);
    }

    public Cloneable<CloneableWrapper<K>> cloneableKey() {
        return cloneableKey;
    }

    public Key<K> cloneableKey(Cloneable<CloneableWrapper<K>> cloneableKey) {
        this.cloneableKey = cloneableKey;
        return this;
    }

    @Override
    public Key<K> fastClone() {
        return new Key(cloneableKey.fastClone().value());
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
        return Objects.hash(key, cloneableKey);
    }


    @Override
    public String toString() {
        return key.toString();
    }
}


