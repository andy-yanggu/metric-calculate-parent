package com.yanggu.metric_calculate.core2.kryo.serializer.table;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.yanggu.metric_calculate.core2.table.SlidingTimeTable;

import java.util.Map;


public class SlidingTimeTableSerializer<IN, ACC, OUT> extends Serializer<SlidingTimeTable<IN, ACC, OUT>> {

    private final DefaultSerializers.LongSerializer longSerializer = new DefaultSerializers.LongSerializer();

    private final MapSerializer<Map> mapMapSerializer = new MapSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, SlidingTimeTable<IN, ACC, OUT> slidingTimeTimeTable) {
        longSerializer.write(kryo, output, slidingTimeTimeTable.getTimestamp());
        mapMapSerializer.write(kryo, output, slidingTimeTimeTable.getMap());
    }

    @Override
    public SlidingTimeTable<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends SlidingTimeTable<IN, ACC, OUT>> clazz) {
        SlidingTimeTable<IN, ACC, OUT> slidingTimeTable = new SlidingTimeTable<>();
        slidingTimeTable.setTimestamp(longSerializer.read(kryo, input, Long.class));
        slidingTimeTable.setMap(mapMapSerializer.read(kryo, input, Map.class));
        return slidingTimeTable;
    }

}
