package com.yanggu.metric_calculate.core.pojo.acc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.StringJoiner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue<K, V> implements Serializable {

    @Serial
    private static final long serialVersionUID = 5095923134278705693L;

    private K key;

    private V value;

    @Override
    public String toString() {
        return new StringJoiner(", ", KeyValue.class.getSimpleName() + "[", "]")
                .add("key=" + key)
                .add("value=" + value)
                .toString();
    }

}
