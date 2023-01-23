package com.yanggu.metric_calculate.core.kryo.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;
import com.yanggu.metric_calculate.core.cube.TimedKVMetricCube;
import com.yanggu.metric_calculate.core.fieldprocess.DimensionSet;
import com.yanggu.metric_calculate.core.fieldprocess.TimeBaselineDimension;


public class TimedKVMetricCubeSerializer extends Serializer<TimedKVMetricCube> {
    @Override
    public void write(Kryo kryo, Output output, TimedKVMetricCube cube) {

        kryo.writeObject(output, cube.getName());
        kryo.writeObject(output, cube.getKey());
        kryo.writeObject(output, cube.getReferenceTime());
        kryo.writeObject(output, cube.getDimensionSet());
        kryo.writeObject(output, cube.getTimeBaselineDimension());
        kryo.writeObject(output, cube.getTable());
    }

    @Override
    public TimedKVMetricCube read(Kryo kryo, Input input, Class<TimedKVMetricCube> type) {

        String name = kryo.readObject(input, String.class);
        String key = kryo.readObject(input, String.class);
        Long referenceTime = kryo.readObject(input, Long.class);
        DimensionSet dimensionSet = kryo.readObject(input, DimensionSet.class);
        TimeBaselineDimension baselineDimension = kryo.readObject(input, TimeBaselineDimension.class);
        TimeSeriesKVTable table = kryo.readObject(input, TimeSeriesKVTable.class);

        TimedKVMetricCube cube = new TimedKVMetricCube<>();
        cube.setName(name);
        cube.setKey(key);
        cube.setReferenceTime(referenceTime);
        cube.setDimensionSet(dimensionSet);
        cube.setTimeBaselineDimension(baselineDimension);
        cube.setTable(table);
        return cube;
    }
}
