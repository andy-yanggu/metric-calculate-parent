package com.yanggu.metric_calculate.flink.process_function;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import com.yanggu.metric_calculate.flink.pojo.DeriveData;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Map;

@Slf4j
public class KeyedBroadcastProcessFunction2 extends KeyedBroadcastProcessFunction<DimensionSet, JSONObject, DeriveData, DeriveMetricCalculateResult>
                implements CheckpointedFunction {

    private static final long serialVersionUID = 6092835299466260638L;
    private final MapStateDescriptor<Long, DeriveData> deriveMapStateDescriptor = new MapStateDescriptor<>("deriveMapState", Long.class, DeriveData.class);

    private transient BroadcastState<Long, DeriveData> broadcastState;

    @Override
    public void processElement(JSONObject value,
                               KeyedBroadcastProcessFunction<DimensionSet, JSONObject, DeriveData, DeriveMetricCalculateResult>.ReadOnlyContext ctx,
                               Collector<DeriveMetricCalculateResult> out) throws Exception {
        Long deriveId = value.getLong("deriveId");
        ReadOnlyBroadcastState<Long, DeriveData> tempBroadcastState = ctx.getBroadcastState(deriveMapStateDescriptor);
        DeriveData deriveData = tempBroadcastState.get(deriveId);
        DeriveMetricCalculate deriveMetricCalculate = deriveData.getDeriveMetricCalculate();
        DimensionSet dimensionSet = ctx.getCurrentKey();
        MetricCube historyMetricCube = value.get("historyMetricCube", MetricCube.class);
        historyMetricCube = deriveMetricCalculate.addInput(value, historyMetricCube, dimensionSet);
        DeriveMetricCalculateResult deriveMetricCalculateResult = historyMetricCube.query();
        if (deriveMetricCalculateResult != null) {
            out.collect(deriveMetricCalculateResult);
        }
    }

    @Override
    public void processBroadcastElement(DeriveData value,
                                        KeyedBroadcastProcessFunction<DimensionSet, JSONObject, DeriveData, DeriveMetricCalculateResult>.Context ctx,
                                        Collector<DeriveMetricCalculateResult> out) throws Exception {
        initDeriveMetricCalculate(value);
        BroadcastState<Long, DeriveData> tempBroadcastState = ctx.getBroadcastState(deriveMapStateDescriptor);
        tempBroadcastState.put(value.getDerive().getId(), value);
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        broadcastState = context.getOperatorStateStore().getBroadcastState(deriveMapStateDescriptor);
        if (context.isRestored()) {
            broadcastState.iterator().forEachRemaining(temp -> initDeriveMetricCalculate(temp.getValue()));
        } else {
        }
    }

    private void initDeriveMetricCalculate(DeriveData deriveData) {
        if (deriveData == null) {
            log.error("传入的deriveData为null");
        }
        Long tableId = deriveData.getTableId();
        Map<String, Class<?>> fieldMap = deriveData.getFieldMap();
        Derive derive = deriveData.getDerive();
        DeriveMetricCalculate deriveMetricCalculate = MetricUtil.initDerive(derive, tableId, fieldMap);
        deriveData.setDeriveMetricCalculate(deriveMetricCalculate);
    }

}
