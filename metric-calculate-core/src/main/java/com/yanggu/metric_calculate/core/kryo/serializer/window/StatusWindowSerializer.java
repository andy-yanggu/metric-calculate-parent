package com.yanggu.metric_calculate.core.kryo.serializer.window;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoCollectionSerializer;
import com.yanggu.metric_calculate.core.window.StatusWindow;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class StatusWindowSerializer<IN, ACC, OUT> extends Serializer<StatusWindow<IN, ACC, OUT>> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3202427902720287246L;

    private final KryoCollectionSerializer<List> statusListSerializer = new KryoCollectionSerializer<>();

    private final KryoCollectionSerializer<List> inListSerializer = new KryoCollectionSerializer<>();

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
