package com.yanggu.metric_calculate.core2.kryo.serializer;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.yanggu.metric_calculate.core2.table.TumblingTimeTimeTable;

import java.util.TreeMap;

public class TimeTableSerializer<IN, ACC, OUT> extends Serializer<TumblingTimeTimeTable<IN, ACC, OUT>> {

    private DefaultSerializers.TreeMapSerializer treeMapSerializer = new DefaultSerializers.TreeMapSerializer();

    @Override
    public void write(Kryo kryo, Output output, TumblingTimeTimeTable<IN, ACC, OUT> object) {
        treeMapSerializer.write(kryo, output, object.getTreeMap());
    }

    @Override
    public TumblingTimeTimeTable<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends TumblingTimeTimeTable<IN, ACC, OUT>> type) {
        TumblingTimeTimeTable<IN, ACC, OUT> tumblingTimeTable = new TumblingTimeTimeTable<>();
        tumblingTimeTable.setTreeMap(treeMapSerializer.read(kryo, input, TreeMap.class));
        return tumblingTimeTable;
    }

}
