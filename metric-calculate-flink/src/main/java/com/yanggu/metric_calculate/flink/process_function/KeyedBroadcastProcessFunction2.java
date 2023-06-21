package com.yanggu.metric_calculate.flink.process_function;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Map;

public class KeyedBroadcastProcessFunction2 extends KeyedBroadcastProcessFunction<DimensionSet, JSONObject, JSONObject, Object> implements CheckpointedFunction {

    private final MapStateDescriptor<Long, JSONObject> deriveMapStateDescriptor = new MapStateDescriptor<>("deriveMapState", Long.class, JSONObject.class);

    private BroadcastState<Long, JSONObject> broadcastState;

    @Override
    public void processElement(JSONObject value, KeyedBroadcastProcessFunction<DimensionSet, JSONObject, JSONObject, Object>.ReadOnlyContext ctx, Collector<Object> out) throws Exception {
        Long deriveId = value.getLong("deriveId");

        ReadOnlyBroadcastState<Long, JSONObject> broadcastState = ctx.getBroadcastState(deriveMapStateDescriptor);
        JSONObject jsonObject = broadcastState.get(deriveId);
        DeriveMetricCalculate deriveMetricCalculate = jsonObject.get("deriveMetricCalculate", DeriveMetricCalculate.class);
        DimensionSet dimensionSet = ctx.getCurrentKey();
        MetricCube historyMetricCube = value.get("historyMetricCube", MetricCube.class);
        historyMetricCube = deriveMetricCalculate.addInput(value, historyMetricCube, dimensionSet);
        DeriveMetricCalculateResult deriveMetricCalculateResult = historyMetricCube.query();
        if (deriveMetricCalculateResult != null) {
            out.collect(deriveMetricCalculateResult);
        }
    }

    @Override
    public void processBroadcastElement(JSONObject value, KeyedBroadcastProcessFunction<DimensionSet, JSONObject, JSONObject, Object>.Context ctx, Collector<Object> out) throws Exception {
        Long tableId = value.getLong("tableId");
        Map<String, Class<?>> fieldMap = (Map<String, Class<?>>) value.get("fieldMap");
        Derive derive = value.get("derive", Derive.class);
        DeriveMetricCalculate<Object, Object, Object> deriveMetricCalculate = MetricUtil.initDerive(derive, tableId, fieldMap);
        value.set("deriveMetricCalculate", deriveMetricCalculate);
        BroadcastState<Long, JSONObject> tempBroadcastState = ctx.getBroadcastState(deriveMapStateDescriptor);
        tempBroadcastState.put(tableId, value);
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        broadcastState.iterator().forEachRemaining(temp -> temp.getValue().remove("deriveMetricCalculate"));
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        broadcastState = context.getOperatorStateStore().getBroadcastState(deriveMapStateDescriptor);
        if (context.isRestored()) {
            broadcastState.iterator().forEachRemaining(temp -> {
                JSONObject value = temp.getValue();
                Long tableId = value.getLong("tableId");
                Map<String, Class<?>> fieldMap = (Map<String, Class<?>>) value.get("fieldMap");
                Derive derive = value.get("derive", Derive.class);
                DeriveMetricCalculate<Object, Object, Object> deriveMetricCalculate = MetricUtil.initDerive(derive, tableId, fieldMap);
                value.set("deriveMetricCalculate", deriveMetricCalculate);
            });
        } else {

        }
    }

}
