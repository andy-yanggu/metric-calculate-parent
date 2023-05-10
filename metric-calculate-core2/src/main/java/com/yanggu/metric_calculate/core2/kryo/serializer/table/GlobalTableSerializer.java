package com.yanggu.metric_calculate.core2.kryo.serializer.table;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core2.table.GlobalTable;

/**
 * 全窗口序列化器
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
public class GlobalTableSerializer<IN, ACC, OUT> extends Serializer<GlobalTable<IN, ACC, OUT>> {

    @Override
    public void write(Kryo kryo, Output output, GlobalTable<IN, ACC, OUT> globalTable) {
        kryo.writeClassAndObject(output, globalTable.getAccumulator());
    }

    @Override
    public GlobalTable<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends GlobalTable<IN, ACC, OUT>> type) {
        Object acc = kryo.readClassAndObject(input);
        GlobalTable<IN, ACC, OUT> globalTable = new GlobalTable<>();
        globalTable.setAccumulator(((ACC) acc));
        return globalTable;
    }

}
