package com.yanggu.metriccalculate.kryo.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.yanggu.metriccalculate.cube.TimeSeriesKVTable;

import java.util.Map;

public class TimeSeriesKVTableSerializer extends MapSerializer {
    @Override
    public void write(Kryo kryo, Output output, Map map) {
        TimeSeriesKVTable table = (TimeSeriesKVTable) map;
        super.write(kryo, output, map);
    }

    @Override
    public TimeSeriesKVTable read(Kryo kryo, Input input, Class<Map> type) {
        TimeSeriesKVTable table = (TimeSeriesKVTable) super.read(kryo, input, type);
        return table;
    }
}
