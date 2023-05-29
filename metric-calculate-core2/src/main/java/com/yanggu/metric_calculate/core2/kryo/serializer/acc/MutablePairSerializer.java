package com.yanggu.metric_calculate.core2.kryo.serializer.acc;

import cn.hutool.core.lang.mutable.MutablePair;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * 可变二元组序列化器
 *
 * @param <K>
 * @param <V>
 */
public class MutablePairSerializer<K, V> extends Serializer<MutablePair<K, V>> {

    @Override
    public void write(Kryo kryo, Output output, MutablePair<K, V> mutablePair) {
        kryo.writeClassAndObject(output, mutablePair.getKey());
        kryo.writeClassAndObject(output, mutablePair.getValue());
    }

    @Override
    public MutablePair<K, V> read(Kryo kryo, Input input, Class<? extends MutablePair<K, V>> clazz) {
        K k = (K) kryo.readClassAndObject(input);
        V v = (V) kryo.readClassAndObject(input);
        return new MutablePair<>(k, v);
    }

}
