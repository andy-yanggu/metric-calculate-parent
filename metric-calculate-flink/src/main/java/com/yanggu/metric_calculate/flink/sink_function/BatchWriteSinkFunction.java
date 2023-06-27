package com.yanggu.metric_calculate.flink.sink_function;

import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.io.Serializable;
import java.util.List;

import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE_METRIC_MIDDLE_STORE;


public class BatchWriteSinkFunction extends RichSinkFunction<List<MetricCube>> implements Serializable {

    private static final long serialVersionUID = 8265578138715615701L;

    private transient DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public void open(Configuration parameters) throws Exception {
        this.deriveMetricMiddleStore = DERIVE_METRIC_MIDDLE_STORE;
    }

    @Override
    public void invoke(List<MetricCube> list, Context context) throws Exception {
        //批量更新
        deriveMetricMiddleStore.batchUpdate(list);
    }

}
