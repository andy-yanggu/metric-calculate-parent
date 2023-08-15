package com.yanggu.metric_calculate.core.kryo.serializer.acc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.dromara.hutool.core.lang.mutable.MutableEntry;

import java.io.Serializable;

/**
 * 可变二元组序列化器
 *
 * @param <K>
 * @param <V>
 */
public class MutableEntrySerializer<K, V> extends Serializer<MutableEntry<K, V>> implements Serializable {

    private static final long serialVersionUID = -2839965751894818964L;

    @Override
    public void write(Kryo kryo, Output output, MutableEntry<K, V> mutablePair) {
        kryo.writeClassAndObject(output, mutablePair.getKey());
        kryo.writeClassAndObject(output, mutablePair.getValue());
    }

    @Override
    public MutableEntry<K, V> read(Kryo kryo, Input input, Class<? extends MutableEntry<K, V>> clazz) {
        K k = (K) kryo.readClassAndObject(input);
        V v = (V) kryo.readClassAndObject(input);
        return new MutableEntry<>(k, v);
    }

}
