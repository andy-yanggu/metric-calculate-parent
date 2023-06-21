package com.yanggu.metric_calculate.flink.process_function;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

public class DeriveCalculateProcessFunction extends KeyedProcessFunction<DimensionSet, JSONObject, JSONObject> {

    private transient Map<Long, DeriveMetricCalculate> deriveMetricCalculateMap = new HashMap<>();

    @Override
    public void open(Configuration parameters) throws Exception {

    }

    @Override
    public void processElement(JSONObject input,
                               KeyedProcessFunction<DimensionSet, JSONObject, JSONObject>.Context ctx,
                               Collector<JSONObject> out) throws Exception {
        DimensionSet dimensionSet = ctx.getCurrentKey();
        Long deriveId = input.getLong("deriveId");
        DeriveMetricCalculate deriveMetricCalculate = deriveMetricCalculateMap.get(deriveId);
        //deriveMetricCalculate.addInput(input, , dimensionSet);

    }

}
