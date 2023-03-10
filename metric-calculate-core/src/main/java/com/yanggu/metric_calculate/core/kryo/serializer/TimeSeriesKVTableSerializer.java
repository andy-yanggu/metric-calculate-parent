package com.yanggu.metric_calculate.core.kryo.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;

public class TimeSeriesKVTableSerializer extends MapSerializer<TimeSeriesKVTable<?>> {

    @Override
    public void write(Kryo kryo, Output output, TimeSeriesKVTable<?> map) {
        kryo.writeObject(output, map.getTimeBaselineDimension());
        super.write(kryo, output, map);
    }

    @Override
    public TimeSeriesKVTable<?> read(Kryo kryo, Input input, Class<? extends TimeSeriesKVTable<?>> type) {
        TimeBaselineDimension timeBaselineDimension = kryo.readObject(input, TimeBaselineDimension.class);
        TimeSeriesKVTable<?> table = super.read(kryo, input, type);
        table.setTimeBaselineDimension(timeBaselineDimension);
        return table;
    }

}
