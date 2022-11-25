package com.yanggu.metriccalculate.kryo.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.yanggu.metriccalculate.cube.TimedKVMetricCube;
import com.yanggu.metriccalculate.fieldprocess.TimeBaselineDimension;

import java.util.LinkedHashMap;

public class TimedKVMetricCubeSerializer extends Serializer<TimedKVMetricCube> {
    @Override
    public void write(Kryo kryo, Output output, TimedKVMetricCube cube) {

        kryo.writeObject(output, cube.name());
        kryo.writeObjectOrNull(output, cube.key(), new DefaultSerializers.StringSerializer());

        kryo.writeObject(output, cube.expire());
        kryo.writeObject(output, cube.referenceTime());

        kryo.writeObject(output, cube.baselineDimension());
        kryo.writeObject(output, cube.dimensions());
        kryo.writeObject(output, cube.table());
    }

    @Override
    public TimedKVMetricCube read(Kryo kryo, Input input, Class<TimedKVMetricCube> type) {

        String name = kryo.readObject(input, String.class);
        String key = kryo.readObjectOrNull(input, String.class);

        Long expire = kryo.readObject(input, Long.class);
        Long referenceTime = kryo.readObject(input, Long.class);

        TimeBaselineDimension baselineDimension = kryo.readObject(input, TimeBaselineDimension.class);
        LinkedHashMap dimensionSet = kryo.readObject(input, LinkedHashMap.class);

        TimedKVMetricCube cube = new TimedKVMetricCube<>(/*name, key, expire, baselineDimension, dimensionSet*/);
        cube.referenceTime(referenceTime);


        //cube.init().table().merge(kryo.readObject(input, TimeSeriesKVTable.class));

        return cube;
    }
}
