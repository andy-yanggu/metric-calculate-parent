package com.yanggu.metric_calculate.flink.process_function;


import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import lombok.Data;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.io.Serializable;
import java.util.List;

import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE_METRIC_MIDDLE_STORE;

@Data
public class BatchUpdateProcessFunction extends ProcessFunction<List<MetricCube>, Void> implements Serializable {

    private static final long serialVersionUID = 8265578138715615701L;

    private transient DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public void open(Configuration parameters) throws Exception {
        this.deriveMetricMiddleStore = DERIVE_METRIC_MIDDLE_STORE;
    }

    @Override
    public void processElement(List<MetricCube> list,
                               ProcessFunction<List<MetricCube>, Void>.Context context,
                               Collector<Void> collector) {

    }

}
