package com.yanggu.metric_calculate.core.kryo.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;
import com.yanggu.metric_calculate.core.pojo.TimeBaselineDimension;

import java.util.Map;

public class TimeSeriesKVTableSerializer extends MapSerializer {

    @Override
    public void write(Kryo kryo, Output output, Map map) {
        super.write(kryo, output, map);
        TimeSeriesKVTable<?> table = (TimeSeriesKVTable<?>) map;
        kryo.writeObject(output, table.getTimeBaselineDimension());
    }

    @Override
    public TimeSeriesKVTable<?> read(Kryo kryo, Input input, Class<Map> type) {
        TimeSeriesKVTable<?> table = (TimeSeriesKVTable<?>) super.read(kryo, input, type);
        table.setTimeBaselineDimension(kryo.readObject(input, TimeBaselineDimension.class));
        return table;
    }

}
