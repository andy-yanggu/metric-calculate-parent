package com.yanggu.metric_calculate.core2.kryo.serializer;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.yanggu.metric_calculate.core2.table.TimeTable;

import java.util.TreeMap;

public class TimeTableSerializer<IN, ACC, OUT> extends Serializer<TimeTable<IN, ACC, OUT>> {

    private DefaultSerializers.TreeMapSerializer treeMapSerializer = new DefaultSerializers.TreeMapSerializer();

    @Override
    public void write(Kryo kryo, Output output, TimeTable<IN, ACC, OUT> object) {
        treeMapSerializer.write(kryo, output, object.getTreeMap());
    }

    @Override
    public TimeTable<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends TimeTable<IN, ACC, OUT>> type) {
        TimeTable<IN, ACC, OUT> timeTable = new TimeTable<>();
        timeTable.setTreeMap(treeMapSerializer.read(kryo, input, TreeMap.class));
        return timeTable;
    }

}
