package com.yanggu.metric_calculate.core.kryo.serializer.acc;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.dromara.hutool.core.lang.tuple.Pair;

import java.io.Serializable;

/**
 * 不可变二元组序列化器
 *
 * @param <K>
 * @param <V>
 */
public class PairSerializer<K, V> extends Serializer<Pair<K, V>> implements Serializable {

    private static final long serialVersionUID = 1928262873772992960L;

    @Override
    public void write(Kryo kryo, Output output, Pair<K, V> pair) {
        kryo.writeClassAndObject(output, pair.getLeft());
        kryo.writeClassAndObject(output, pair.getRight());
    }

    @Override
    public Pair<K, V> read(Kryo kryo, Input input, Class<? extends Pair<K, V>> type) {
        K key = (K) kryo.readClassAndObject(input);
        V value = (V) kryo.readClassAndObject(input);
        return Pair.of(key, value);
    }

}
