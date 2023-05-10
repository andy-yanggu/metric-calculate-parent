package com.yanggu.metric_calculate.core2.kryo.serializer.table;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.yanggu.metric_calculate.core2.table.PatternTable;

import java.util.TreeMap;


public class PatternTableSerializer<IN, ACC, OUT> extends Serializer<PatternTable<IN, ACC, OUT>> {

    private final DefaultSerializers.LongSerializer longSerializer = new DefaultSerializers.LongSerializer();

    private final DefaultSerializers.TreeMapSerializer mapMapSerializer = new DefaultSerializers.TreeMapSerializer();

    @Override
    public void write(Kryo kryo, Output output, PatternTable<IN, ACC, OUT> object) {
        longSerializer.write(kryo, output, object.getTimestamp());
        mapMapSerializer.write(kryo, output, object.getDataMap());
    }

    @Override
    public PatternTable<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends PatternTable<IN, ACC, OUT>> type) {
        PatternTable<IN, ACC, OUT> patternTable = new PatternTable<>();
        patternTable.setTimestamp(longSerializer.read(kryo, input, Long.class));
        patternTable.setDataMap(mapMapSerializer.read(kryo, input, TreeMap.class));
        return patternTable;
    }

}
