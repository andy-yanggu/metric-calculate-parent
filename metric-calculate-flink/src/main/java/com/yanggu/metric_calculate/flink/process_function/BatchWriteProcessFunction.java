package com.yanggu.metric_calculate.flink.process_function;

import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.io.Serializable;
import java.util.List;

import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE_METRIC_MIDDLE_STORE;


public class BatchWriteProcessFunction extends ProcessFunction<List<MetricCube>, MetricCube> implements Serializable {

    private static final long serialVersionUID = 8265578138715615701L;

    private transient DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public void open(Configuration parameters) throws Exception {
        this.deriveMetricMiddleStore = DERIVE_METRIC_MIDDLE_STORE;
    }

    @Override
    public void processElement(List<MetricCube> list,
                               ProcessFunction<List<MetricCube>, MetricCube>.Context ctx,
                               Collector<MetricCube> out) throws Exception {
        //批量更新
        deriveMetricMiddleStore.batchUpdate(list);
        //发送到下游
        list.forEach(out::collect);
    }

}
