package com.yanggu.metric_calculate.core2.kryo.serializer.window;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.yanggu.metric_calculate.core2.window.TumblingTimeWindow;

import java.io.Serializable;
import java.util.TreeMap;

public class TumblingTimeWindowSerializer<IN, ACC, OUT> extends Serializer<TumblingTimeWindow<IN, ACC, OUT>> implements Serializable {

    private static final long serialVersionUID = 3500078827166722503L;

    private final DefaultSerializers.LongSerializer longSerializer = new DefaultSerializers.LongSerializer();

    private final DefaultSerializers.TreeMapSerializer treeMapSerializer = new DefaultSerializers.TreeMapSerializer();

    @Override
    public void write(Kryo kryo, Output output, TumblingTimeWindow<IN, ACC, OUT> tumblingTimeWindow) {
        longSerializer.write(kryo, output, tumblingTimeWindow.getTimestamp());
        treeMapSerializer.write(kryo, output, tumblingTimeWindow.getTreeMap());
    }

    @Override
    public TumblingTimeWindow<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends TumblingTimeWindow<IN, ACC, OUT>> type) {
        TumblingTimeWindow<IN, ACC, OUT> tumblingTimeWindow = new TumblingTimeWindow<>();
        tumblingTimeWindow.setTimestamp(longSerializer.read(kryo, input, Long.class));
        tumblingTimeWindow.setTreeMap(treeMapSerializer.read(kryo, input, TreeMap.class));
        return tumblingTimeWindow;
    }

}
