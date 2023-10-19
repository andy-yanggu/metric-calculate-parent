package com.yanggu.metric_calculate.core.kryo.serializer.acc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.dromara.hutool.core.lang.tuple.Tuple;

import java.io.Serial;
import java.io.Serializable;

/**
 * 元祖序列化器
 */
public class TupleSerializer extends Serializer<Tuple> implements Serializable {

    @Serial
    private static final long serialVersionUID = -5150747248969279645L;

    @Override
    public void write(Kryo kryo, Output output, Tuple object) {
        int size = object.size();
        output.writeInt(size);
        for (int i = 0; i < size; i++) {
            kryo.writeClassAndObject(output, object.get(i));
        }
    }

    @Override
    public Tuple read(Kryo kryo, Input input, Class<? extends Tuple> type) {
        int size = input.readInt();
        Object[] objects = new Object[size];
        for (int i = 0; i < size; i++) {
            objects[i] = kryo.readClassAndObject(input);
        }
        return new Tuple(objects);
    }

}
