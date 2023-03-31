package com.yanggu.metric_calculate.core2.kryo.serializer;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.yanggu.metric_calculate.core2.table.TimeTable2;

import java.util.TreeMap;

public class TimeTableSerializer<IN, ACC, OUT> extends Serializer<TimeTable2<IN, ACC, OUT>> {

    private DefaultSerializers.TreeMapSerializer treeMapSerializer = new DefaultSerializers.TreeMapSerializer();

    @Override
    public void write(Kryo kryo, Output output, TimeTable2<IN, ACC, OUT> object) {
        treeMapSerializer.write(kryo, output, object.getTreeMap());
    }

    @Override
    public TimeTable2<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends TimeTable2<IN, ACC, OUT>> type) {
        TimeTable2<IN, ACC, OUT> timeTable = new TimeTable2<>();
        timeTable.setTreeMap(treeMapSerializer.read(kryo, input, TreeMap.class));
        return timeTable;
    }

}
