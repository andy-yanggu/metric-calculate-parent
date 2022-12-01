package com.yanggu.metriccalculate.kryo.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.yanggu.metriccalculate.cube.TimeSeriesKVTable;
import com.yanggu.metriccalculate.cube.TimedKVMetricCube;
import com.yanggu.metriccalculate.fieldprocess.DimensionSet;
import com.yanggu.metriccalculate.fieldprocess.TimeBaselineDimension;


public class TimedKVMetricCubeSerializer extends Serializer<TimedKVMetricCube> {
    @Override
    public void write(Kryo kryo, Output output, TimedKVMetricCube cube) {

        kryo.writeObject(output, cube.name());
        kryo.writeObjectOrNull(output, cube.key(), new DefaultSerializers.StringSerializer());

        kryo.writeObject(output, cube.expire());
        kryo.writeObject(output, cube.getReferenceTime());

        kryo.writeObject(output, cube.baselineDimension());
        kryo.writeObject(output, cube.dimensions());
        kryo.writeObject(output, cube.table());
    }

    @Override
    public TimedKVMetricCube read(Kryo kryo, Input input, Class<TimedKVMetricCube> type) {

        String name = kryo.readObject(input, String.class);

        Long referenceTime = kryo.readObject(input, Long.class);

        DimensionSet dimensionSet = kryo.readObject(input, DimensionSet.class);

        TimeBaselineDimension baselineDimension = kryo.readObject(input, TimeBaselineDimension.class);

        TimeSeriesKVTable table = kryo.readObject(input, TimeSeriesKVTable.class);

        TimedKVMetricCube cube = new TimedKVMetricCube<>();
        cube.setReferenceTime(referenceTime);

        return cube;
    }
}
