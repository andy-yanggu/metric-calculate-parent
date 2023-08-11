package com.yanggu.metric_calculate.core.kryo.serializer.window;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.window.GlobalWindow;

import java.io.Serializable;

/**
 * 全窗口序列化器
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
public class GlobalWindowSerializer<IN, ACC, OUT> extends Serializer<GlobalWindow<IN, ACC, OUT>> implements Serializable {

    private static final long serialVersionUID = 7154340627620305186L;

    @Override
    public void write(Kryo kryo, Output output, GlobalWindow<IN, ACC, OUT> globalWindow) {
        kryo.writeClassAndObject(output, globalWindow.getAccumulator());
    }

    @Override
    public GlobalWindow<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends GlobalWindow<IN, ACC, OUT>> type) {
        Object acc = kryo.readClassAndObject(input);
        GlobalWindow<IN, ACC, OUT> globalWindow = new GlobalWindow<>();
        globalWindow.setAccumulator(((ACC) acc));
        return globalWindow;
    }

}
