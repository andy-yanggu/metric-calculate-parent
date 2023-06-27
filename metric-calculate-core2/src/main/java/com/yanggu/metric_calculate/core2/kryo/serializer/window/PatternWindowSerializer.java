package com.yanggu.metric_calculate.core2.kryo.serializer.window;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core2.kryo.serializer.util.KryoLongSerializer;
import com.yanggu.metric_calculate.core2.kryo.serializer.util.KryoTreeMapSerializer;
import com.yanggu.metric_calculate.core2.window.PatternWindow;

import java.io.Serializable;
import java.util.TreeMap;


public class PatternWindowSerializer<IN, ACC, OUT> extends Serializer<PatternWindow<IN, ACC, OUT>> implements Serializable {

    private static final long serialVersionUID = 7195228733772121102L;

    private final KryoLongSerializer longSerializer = new KryoLongSerializer();

    private final KryoTreeMapSerializer treeMapSerializer = new KryoTreeMapSerializer();

    @Override
    public void write(Kryo kryo, Output output, PatternWindow<IN, ACC, OUT> patternWindow) {
        longSerializer.write(kryo, output, patternWindow.getTimestamp());
        treeMapSerializer.write(kryo, output, patternWindow.getDataMap());
    }

    @Override
    public PatternWindow<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends PatternWindow<IN, ACC, OUT>> type) {
        PatternWindow<IN, ACC, OUT> patternWindow = new PatternWindow<>();
        patternWindow.setTimestamp(longSerializer.read(kryo, input, Long.class));
        patternWindow.setDataMap(treeMapSerializer.read(kryo, input, TreeMap.class));
        return patternWindow;
    }

}
