package com.yanggu.metric_calculate.core.kryo.serializer.acc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.dromara.hutool.core.lang.mutable.MutablePair;

import java.io.Serial;
import java.io.Serializable;

/**
 * 可变二元组序列化器
 *
 * @param <K>
 * @param <V>
 */
public class MutablePairSerializer<K, V> extends Serializer<MutablePair<K, V>> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2839965751894818964L;

    @Override
    public void write(Kryo kryo, Output output, MutablePair<K, V> mutablePair) {
        kryo.writeClassAndObject(output, mutablePair.getLeft());
        kryo.writeClassAndObject(output, mutablePair.getRight());
    }

    @Override
    public MutablePair<K, V> read(Kryo kryo, Input input, Class<? extends MutablePair<K, V>> clazz) {
        K k = (K) kryo.readClassAndObject(input);
        V v = (V) kryo.readClassAndObject(input);
        return new MutablePair<>(k, v);
    }

}
