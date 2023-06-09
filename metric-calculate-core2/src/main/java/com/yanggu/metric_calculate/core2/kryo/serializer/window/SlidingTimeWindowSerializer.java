package com.yanggu.metric_calculate.core2.kryo.serializer.window;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.yanggu.metric_calculate.core2.window.SlidingTimeWindow;

import java.util.Map;


public class SlidingTimeWindowSerializer<IN, ACC, OUT> extends Serializer<SlidingTimeWindow<IN, ACC, OUT>> {

    private final DefaultSerializers.LongSerializer longSerializer = new DefaultSerializers.LongSerializer();

    private final MapSerializer<Map> mapSerializer = new MapSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, SlidingTimeWindow<IN, ACC, OUT> slidingTimeWindow) {
        longSerializer.write(kryo, output, slidingTimeWindow.getTimestamp());
        mapSerializer.write(kryo, output, slidingTimeWindow.getMap());
    }

    @Override
    public SlidingTimeWindow<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends SlidingTimeWindow<IN, ACC, OUT>> clazz) {
        SlidingTimeWindow<IN, ACC, OUT> slidingTimeWindow = new SlidingTimeWindow<>();
        slidingTimeWindow.setTimestamp(longSerializer.read(kryo, input, Long.class));
        slidingTimeWindow.setMap(mapSerializer.read(kryo, input, Map.class));
        return slidingTimeWindow;
    }

}
