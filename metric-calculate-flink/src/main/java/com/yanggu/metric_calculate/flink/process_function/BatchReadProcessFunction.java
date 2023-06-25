package com.yanggu.metric_calculate.flink.process_function;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import lombok.Data;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.flink.util.Constant.DIMENSION_SET;
import static com.yanggu.metric_calculate.flink.util.Constant.HISTORY_METRIC_CUBE;

/**
 * 攒批读
 */
@Data
public class BatchReadProcessFunction extends ProcessFunction<List<JSONObject>, JSONObject> implements Serializable {

    private static final long serialVersionUID = -3855414494042599733L;

    private transient DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public void processElement(List<JSONObject> inputList,
                               ProcessFunction<List<JSONObject>, JSONObject>.Context ctx,
                               Collector<JSONObject> out) {
        List<DimensionSet> collect = inputList.stream()
                .map(temp -> temp.get(DIMENSION_SET, DimensionSet.class))
                .collect(Collectors.toList());
        Map<DimensionSet, MetricCube> dimensionSetMetricCubeMap = deriveMetricMiddleStore.batchGet(collect);
        for (JSONObject input : inputList) {
            DimensionSet dimensionSet = input.get(DIMENSION_SET, DimensionSet.class);
            MetricCube historyMetricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            input.set(HISTORY_METRIC_CUBE, historyMetricCube);
            out.collect(input);
        }
    }

}
