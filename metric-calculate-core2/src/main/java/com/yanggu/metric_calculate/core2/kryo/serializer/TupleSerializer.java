package com.yanggu.metric_calculate.core2.kryo.serializer;

import cn.hutool.core.lang.Tuple;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;


public class TupleSerializer extends Serializer<Tuple> {
    @Override
    public void write(Kryo kryo, Output output, Tuple object) {
        int size = object.size();
        kryo.writeObject(output, size);
        for (int i = 0; i < size; i++) {
            kryo.writeClassAndObject(output, object.get(i));
        }
    }

    @Override
    public Tuple read(Kryo kryo, Input input, Class<? extends Tuple> type) {
        Integer size = kryo.readObject(input, Integer.class);
        Object[] objects = new Object[size];
        for (int i = 0; i < size; i++) {
            objects[i] = kryo.readClassAndObject(input);
        }
        return new Tuple(objects);
    }

}
