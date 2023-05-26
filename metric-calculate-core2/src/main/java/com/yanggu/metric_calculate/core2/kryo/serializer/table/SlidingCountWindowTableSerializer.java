package com.yanggu.metric_calculate.core2.kryo.serializer.table;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.yanggu.metric_calculate.core2.table.SlidingCountWindowTable;

import java.util.ArrayList;
import java.util.List;


public class SlidingCountWindowTableSerializer<IN, ACC, OUT> extends Serializer<SlidingCountWindowTable<IN, ACC, OUT>> {

    private final DefaultSerializers.IntSerializer limitSerializer = new DefaultSerializers.IntSerializer();

    private final CollectionSerializer<List> inListSerializer = new CollectionSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, SlidingCountWindowTable<IN, ACC, OUT> object) {
        limitSerializer.write(kryo, output, object.getLimit());
        inListSerializer.write(kryo, output, object.getInList());
    }

    @Override
    public SlidingCountWindowTable<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends SlidingCountWindowTable<IN, ACC, OUT>> type) {
        SlidingCountWindowTable<IN, ACC, OUT> slidingCountWindowTable = new SlidingCountWindowTable<>();
        slidingCountWindowTable.setLimit(limitSerializer.read(kryo, input, Integer.class));
        slidingCountWindowTable.setInList(inListSerializer.read(kryo, input, ArrayList.class));
        return slidingCountWindowTable;
    }

}
