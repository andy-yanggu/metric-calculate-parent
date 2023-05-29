package com.yanggu.metric_calculate.core2.kryo.serializer.acc;


import cn.hutool.core.lang.Pair;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * 不可变二元组序列化器
 *
 * @param <K>
 * @param <V>
 */
public class PairSerializer<K, V> extends Serializer<Pair<K, V>> {

    @Override
    public void write(Kryo kryo, Output output, Pair<K, V> pair) {
        kryo.writeClassAndObject(output, pair.getKey());
        kryo.writeClassAndObject(output, pair.getValue());
    }

    @Override
    public Pair<K, V> read(Kryo kryo, Input input, Class<? extends Pair<K, V>> type) {
        K key = (K) kryo.readClassAndObject(input);
        V value = (V) kryo.readClassAndObject(input);
        return Pair.of(key, value);
    }

}
