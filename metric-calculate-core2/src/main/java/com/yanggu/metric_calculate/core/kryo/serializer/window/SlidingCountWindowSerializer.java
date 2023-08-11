package com.yanggu.metric_calculate.core.kryo.serializer.window;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoCollectionSerializer;
import com.yanggu.metric_calculate.core.window.SlidingCountWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SlidingCountWindowSerializer<IN, ACC, OUT> extends Serializer<SlidingCountWindow<IN, ACC, OUT>> implements Serializable {

    private static final long serialVersionUID = 6433262404195009833L;

    private final KryoCollectionSerializer<List> inListSerializer = new KryoCollectionSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, SlidingCountWindow<IN, ACC, OUT> slidingCountWindow) {
        output.writeInt(slidingCountWindow.getLimit());
        inListSerializer.write(kryo, output, slidingCountWindow.getInList());
    }

    @Override
    public SlidingCountWindow<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends SlidingCountWindow<IN, ACC, OUT>> type) {
        SlidingCountWindow<IN, ACC, OUT> slidingCountWindow = new SlidingCountWindow<>();
        slidingCountWindow.setLimit(input.readInt());
        slidingCountWindow.setInList(inListSerializer.read(kryo, input, ArrayList.class));
        return slidingCountWindow;
    }

}
