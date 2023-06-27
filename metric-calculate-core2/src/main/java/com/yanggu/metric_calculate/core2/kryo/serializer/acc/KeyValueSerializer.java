package com.yanggu.metric_calculate.core2.kryo.serializer.acc;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core2.util.KeyValue;

import java.io.Serializable;

/**
 * KeyValue数据序列化器
 *
 * @param <K>
 * @param <V>
 */
public class KeyValueSerializer<K extends Comparable<K>, V> extends Serializer<KeyValue<K, V>> implements Serializable {

    private static final long serialVersionUID = -8925969254953524456L;

    @Override
    public void write(Kryo kryo, Output output, KeyValue<K, V> keyValue) {
        kryo.writeClassAndObject(output, keyValue.getKey());
        kryo.writeClassAndObject(output, keyValue.getValue());
    }

    @Override
    public KeyValue<K, V> read(Kryo kryo, Input input, Class<? extends KeyValue<K, V>> type) {
        K k = (K) kryo.readClassAndObject(input);
        V v = (V) kryo.readClassAndObject(input);
        return new KeyValue<>(k, v);
    }

}
