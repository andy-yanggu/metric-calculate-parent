package com.yanggu.metric_calculate.core2.kryo.serializer.window;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.yanggu.metric_calculate.core2.window.StatusWindow;

import java.util.ArrayList;
import java.util.List;


public class StatusWindowSerializer<IN, ACC, OUT> extends Serializer<StatusWindow<IN, ACC, OUT>> {

    private final CollectionSerializer<List> statusListSerializer = new CollectionSerializer<>();

    private final CollectionSerializer<List> inListSerializer = new CollectionSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, StatusWindow<IN, ACC, OUT> statusWindow) {
        statusListSerializer.write(kryo, output, statusWindow.getStatusList());
        inListSerializer.write(kryo, output, statusWindow.getInList());
    }

    @Override
    public StatusWindow<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends StatusWindow<IN, ACC, OUT>> type) {
        StatusWindow<IN, ACC, OUT> statusWindow = new StatusWindow<>();
        statusWindow.setStatusList(statusListSerializer.read(kryo, input, ArrayList.class));
        statusWindow.setInList(inListSerializer.read(kryo, input, ArrayList.class));
        return statusWindow;
    }

}
