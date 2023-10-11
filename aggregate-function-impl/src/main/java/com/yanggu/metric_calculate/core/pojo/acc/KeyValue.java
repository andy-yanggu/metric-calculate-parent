package com.yanggu.metric_calculate.core.pojo.acc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue<K extends Comparable<K>, V> implements Comparable<KeyValue<K, V>> {

    private K key;

    private V value;

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
