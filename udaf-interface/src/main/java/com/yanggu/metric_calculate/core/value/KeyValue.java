package com.yanggu.metric_calculate.core.value;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class KeyValue<K extends Comparable<K> & Cloneable2<K>, V extends Cloneable2<V> & Value>
        implements Value<Map<K, Object>>, Cloneable2<KeyValue<K, V>>, Comparable<KeyValue<K, V>> {

    private K key;

    private V value;

    public KeyValue() {
    }

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue(Comparable key, Object value) {
        this.key = (K) new Key(key);
        this.value = (V) Cloneable2Wrapper.wrap(value);
    }

    public K key() {
        return key;
    }

    public KeyValue<K, V> key(K key) {
        this.key = key;
        return this;
    }

    @Override
    public Map<K, Object> value() {
        Map<K, Object> result = new HashMap<>();
        result.put(key, value.value());
        return result;
    }

    public V getValue() {
        return value;
    }

    @Override
    public KeyValue<K, V> fastClone() {
        return new KeyValue<>(key.fastClone(), value.fastClone());
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

        if (!key.equals(that.key)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        /*int result = key.hashCode();
        result = 31 * result + value.hashCode();*/
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
