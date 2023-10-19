package com.yanggu.metric_calculate.core.kryo.serializer.window;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoMapSerializer;
import com.yanggu.metric_calculate.core.window.SlidingTimeWindow;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;


public class SlidingTimeWindowSerializer<IN, ACC, OUT> extends Serializer<SlidingTimeWindow<IN, ACC, OUT>> implements Serializable {

    @Serial
    private static final long serialVersionUID = -416111087144999336L;

    private final KryoMapSerializer<Map> mapSerializer = new KryoMapSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, SlidingTimeWindow<IN, ACC, OUT> slidingTimeWindow) {
        output.writeLong(slidingTimeWindow.getTimestamp());
        mapSerializer.write(kryo, output, slidingTimeWindow.getMap());
    }

    @Override
    public SlidingTimeWindow<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends SlidingTimeWindow<IN, ACC, OUT>> clazz) {
        SlidingTimeWindow<IN, ACC, OUT> slidingTimeWindow = new SlidingTimeWindow<>();
        slidingTimeWindow.setTimestamp(input.readLong());
        slidingTimeWindow.setMap(mapSerializer.read(kryo, input, Map.class));
        return slidingTimeWindow;
    }

}
