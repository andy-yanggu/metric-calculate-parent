package com.yanggu.metric_calculate.core2.util;

import lombok.NoArgsConstructor;

import java.util.StringJoiner;

@NoArgsConstructor
public class KeyValue<K extends Comparable<K>, V> implements Comparable<KeyValue<K, V>> {

    private K key;

    private V value;

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int compareTo(KeyValue<K, V> that) {
        return key.compareTo(that.key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KeyValue<?, ?> that = (KeyValue<?, ?>) o;

        return this.key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", KeyValue.class.getSimpleName() + "[", "]")
                .add("key=" + key)
                .add("value=" + value)
                .toString();
    }

}
