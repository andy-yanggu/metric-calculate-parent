package com.yanggu.metric_calculate.core.kryo.serializer.acc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.dromara.hutool.core.lang.mutable.MutableObj;

import java.io.Serial;
import java.io.Serializable;

/**
 * 可变对象序列化器
 *
 * @param <T>
 */
public class MutableObjectSerializer<T> extends Serializer<MutableObj<T>> implements Serializable {

    @Serial
    private static final long serialVersionUID = -5624609536031018473L;

    @Override
    public void write(Kryo kryo, Output output, MutableObj<T> mutableObj) {
        kryo.writeClassAndObject(output, mutableObj.get());
    }

    @Override
    public MutableObj<T> read(Kryo kryo, Input input, Class<? extends MutableObj<T>> clazz) {
        MutableObj<T> mutableObj = new MutableObj<>();
        Object o = kryo.readClassAndObject(input);
        mutableObj.set((T) o);
        return mutableObj;
    }

}
