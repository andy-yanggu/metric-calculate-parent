package com.yanggu.metric_calculate.core2.kryo.serializer.cube;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.window.AbstractWindow;

/**
 * 指标数据序列化器
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
public class MetricCubeSerializer<IN, ACC, OUT> extends Serializer<MetricCube<IN, ACC, OUT>> {

    private final DimensionSetSerializer dimensionSetSerializer = new DimensionSetSerializer();

    @Override
    public void write(Kryo kryo, Output output, MetricCube<IN, ACC, OUT> metricCube) {
        kryo.writeObject(output, metricCube.getDimensionSet(), dimensionSetSerializer);
        kryo.writeClassAndObject(output, metricCube.getWindow());
    }

    @Override
    public MetricCube<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends MetricCube<IN, ACC, OUT>> type) {
        DimensionSet dimensionSet = kryo.readObject(input, DimensionSet.class, dimensionSetSerializer);
        AbstractWindow<IN, ACC, OUT> window = (AbstractWindow<IN, ACC, OUT>) kryo.readClassAndObject(input);

        MetricCube<IN, ACC, OUT> metricCube = new MetricCube<>();
        metricCube.setDimensionSet(dimensionSet);
        metricCube.setWindow(window);
        return metricCube;
    }

}
