package com.yanggu.metric_calculate.flink.process_function;

import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.flink.pojo.DeriveCalculateData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE_METRIC_MIDDLE_STORE;

/**
 * 攒批读
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BatchReadProcessFunction extends ProcessFunction<List<DeriveCalculateData>, DeriveCalculateData> implements Serializable {

    @Serial
    private static final long serialVersionUID = -3855414494042599733L;

    private transient DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public void open(Configuration parameters) {
        this.deriveMetricMiddleStore = DERIVE_METRIC_MIDDLE_STORE;
    }

    @Override
    public void processElement(List<DeriveCalculateData> inputList,
                               ProcessFunction<List<DeriveCalculateData>, DeriveCalculateData>.Context ctx,
                               Collector<DeriveCalculateData> out) throws Exception {
        List<DimensionSet> collect = inputList.stream()
                .map(DeriveCalculateData::getDimensionSet)
                .distinct()
                .toList();
        Map<DimensionSet, MetricCube> dimensionSetMetricCubeMap = deriveMetricMiddleStore.batchGet(collect);
        for (DeriveCalculateData input : inputList) {
            DimensionSet dimensionSet = input.getDimensionSet();
            MetricCube historyMetricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            if (historyMetricCube != null) {
                input.setMetricCube(historyMetricCube);
            }
            out.collect(input);
        }
    }

}
