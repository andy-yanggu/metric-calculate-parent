package com.yanggu.metric_calculate.flink.process_function;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleHashMapKryoStore;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MyProcessFunction1 extends ProcessFunction<List<JSONObject>, JSONObject> {

    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public void open(Configuration parameters) throws Exception {
        DeriveMetricMiddleHashMapKryoStore deriveMetricMiddleHashMapKryoStore = new DeriveMetricMiddleHashMapKryoStore();
        deriveMetricMiddleHashMapKryoStore.init();
        this.deriveMetricMiddleStore = deriveMetricMiddleHashMapKryoStore;
    }

    @Override
    public void processElement(List<JSONObject> inputList,
                               ProcessFunction<List<JSONObject>, JSONObject>.Context ctx,
                               Collector<JSONObject> out) throws Exception {
        List<DimensionSet> collect = inputList.stream()
                .map(temp -> temp.get("dimensionSet", DimensionSet.class))
                .collect(Collectors.toList());
        Map<DimensionSet, MetricCube> dimensionSetMetricCubeMap = deriveMetricMiddleStore.batchGet(collect);
        for (JSONObject input : inputList) {
            DimensionSet dimensionSet = input.get("dimensionSet", DimensionSet.class);
            MetricCube historyMetricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            input.set("historyMetricCube", historyMetricCube);
            out.collect(input);
        }
    }

}
