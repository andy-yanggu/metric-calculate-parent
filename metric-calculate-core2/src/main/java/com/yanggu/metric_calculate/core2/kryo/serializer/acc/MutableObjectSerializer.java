package com.yanggu.metric_calculate.core2.kryo.serializer.acc;

import cn.hutool.core.lang.mutable.MutableObj;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;


public class MutableObjectSerializer<T> extends Serializer<MutableObj<T>> {

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
