package com.yanggu.metric_calculate.core.kryo.serializer.cube;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * 维度序列化器
 */
public class DimensionSetSerializer extends Serializer<DimensionSet> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1812423076780276120L;

    @Override
    public void write(Kryo kryo, Output output, DimensionSet dimensionSet) {
        output.writeString(dimensionSet.getKey());
        output.writeString(dimensionSet.getMetricName());
        kryo.writeObject(output, dimensionSet.getDimensionMap());
    }

    @Override
    public DimensionSet read(Kryo kryo, Input input, Class<? extends DimensionSet> type) {
        String key = input.readString();
        String metricName = input.readString();
        LinkedHashMap<String, Object> linkedHashMap = kryo.readObject(input, LinkedHashMap.class);
        return new DimensionSet(key, metricName, linkedHashMap);
    }

}
