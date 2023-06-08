package com.yanggu.metric_calculate.core2.kryo.serializer.table;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.yanggu.metric_calculate.core2.table.StatusWindowTable;

import java.util.ArrayList;
import java.util.List;


public class StatusWindowTableSerializer<IN, ACC, OUT> extends Serializer<StatusWindowTable<IN, ACC, OUT>> {

    private final CollectionSerializer<List> statusListSerializer = new CollectionSerializer<>();

    private final CollectionSerializer<List> inListSerializer = new CollectionSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, StatusWindowTable<IN, ACC, OUT> object) {
        statusListSerializer.write(kryo, output, object.getStatusList());
        inListSerializer.write(kryo, output, object.getInList());
    }

    @Override
    public StatusWindowTable<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends StatusWindowTable<IN, ACC, OUT>> type) {
        StatusWindowTable<IN, ACC, OUT> statusWindowTable = new StatusWindowTable<>();
        statusWindowTable.setStatusList(statusListSerializer.read(kryo, input, ArrayList.class));
        statusWindowTable.setInList(inListSerializer.read(kryo, input, ArrayList.class));
        return statusWindowTable;
    }

}
