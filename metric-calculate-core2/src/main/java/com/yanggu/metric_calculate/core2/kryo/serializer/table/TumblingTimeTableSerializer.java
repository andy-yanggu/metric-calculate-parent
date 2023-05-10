package com.yanggu.metric_calculate.core2.kryo.serializer.table;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.yanggu.metric_calculate.core2.table.TumblingTimeTable;

import java.util.TreeMap;

public class TumblingTimeTableSerializer<IN, ACC, OUT> extends Serializer<TumblingTimeTable<IN, ACC, OUT>> {

    private final DefaultSerializers.LongSerializer longSerializer = new DefaultSerializers.LongSerializer();

    private final DefaultSerializers.TreeMapSerializer treeMapSerializer = new DefaultSerializers.TreeMapSerializer();

    @Override
    public void write(Kryo kryo, Output output, TumblingTimeTable<IN, ACC, OUT> tumblingTimeTable) {
        longSerializer.write(kryo, output, tumblingTimeTable.getTimestamp());
        treeMapSerializer.write(kryo, output, tumblingTimeTable.getTreeMap());
    }

    @Override
    public TumblingTimeTable<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends TumblingTimeTable<IN, ACC, OUT>> type) {
        TumblingTimeTable<IN, ACC, OUT> tumblingTimeTable = new TumblingTimeTable<>();
        tumblingTimeTable.setTimestamp(longSerializer.read(kryo, input, Long.class));
        tumblingTimeTable.setTreeMap(treeMapSerializer.read(kryo, input, TreeMap.class));
        return tumblingTimeTable;
    }

}
