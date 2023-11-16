package com.yanggu.metric_calculate.core.middle_store;


import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.kryo.KryoUtil;
import lombok.Data;

@Data
public abstract class AbstractDeriveMetricMiddleStore implements DeriveMetricMiddleStore {

    private KryoUtil kryoUtil;

    protected <T> T deserialize(byte[] bytes) {
        return kryoUtil.deserialize(bytes);
    }

    /**
     * 序列化之前进行判空处理
     *
     * @param updateMetricCube
     * @return
     * @param <IN>
     * @param <ACC>
     * @param <OUT>
     */
    protected <IN, ACC, OUT> byte[] serialize(MetricCube<IN, ACC, OUT> updateMetricCube) {
        if (updateMetricCube == null) {
            throw new RuntimeException("传入的数据为空");
        }
        if (updateMetricCube.isEmpty()) {
            throw new RuntimeException("传入的table为空");
        }
        return kryoUtil.serialize(updateMetricCube);
    }

}
