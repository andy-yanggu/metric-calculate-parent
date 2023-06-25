package com.yanggu.metric_calculate.flink.process_function;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE_ID;
import static com.yanggu.metric_calculate.flink.util.Constant.HISTORY_METRIC_CUBE;

@Slf4j
public class KeyedDeriveBroadcastProcessFunction extends KeyedBroadcastProcessFunction<DimensionSet, JSONObject, DeriveData, DeriveMetricCalculateResult>
                implements CheckpointedFunction, Serializable {

    private static final long serialVersionUID = 6092835299466260638L;

    private String url = "http://localhost:8888/mock-model/all-derive-data";

    private final MapStateDescriptor<Long, DeriveData> deriveMapStateDescriptor = new MapStateDescriptor<>("deriveMapState", Long.class, DeriveData.class);

    @Override
    public void processElement(JSONObject value,
                               KeyedBroadcastProcessFunction<DimensionSet, JSONObject, DeriveData, DeriveMetricCalculateResult>.ReadOnlyContext ctx,
                               Collector<DeriveMetricCalculateResult> out) throws Exception {
        Long deriveId = value.getLong(DERIVE_ID);
        ReadOnlyBroadcastState<Long, DeriveData> broadcastState = ctx.getBroadcastState(deriveMapStateDescriptor);
        DeriveData deriveData = broadcastState.get(deriveId);
        DeriveMetricCalculate deriveMetricCalculate = deriveData.getDeriveMetricCalculate();
        DimensionSet dimensionSet = ctx.getCurrentKey();
        MetricCube historyMetricCube = value.get(HISTORY_METRIC_CUBE, MetricCube.class);
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
        BroadcastState<Long, DeriveData> broadcastState = ctx.getBroadcastState(deriveMapStateDescriptor);
        broadcastState.put(value.getDerive().getId(), value);
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        BroadcastState<Long, DeriveData> broadcastState = context.getOperatorStateStore().getBroadcastState(deriveMapStateDescriptor);
        if (context.isRestored()) {
            broadcastState.iterator().forEachRemaining(temp -> initDeriveMetricCalculate(temp.getValue()));
        } else {
            //获取所有的派生指标数据
            String jsonArrayString = HttpUtil.get(url);
            if (StrUtil.isBlank(jsonArrayString)) {
                return;
            }
            JSONArray objects = JSONUtil.parseArray(jsonArrayString);
            List<DeriveData> list = new ArrayList<>();
            for (Object object : objects) {
                JSONObject tempObj = (JSONObject) object;
                Long tableId = tempObj.getLong("tableId");
                JSONObject tempFieldMap = tempObj.getJSONObject("fieldMap");
                Derive derive = tempObj.get("derive", Derive.class);
                Map<String, Class<?>> fieldMap = new HashMap<>();
                for (Map.Entry<String, Object> entry : tempFieldMap) {
                    String tempKey = entry.getKey();
                    Object tempValue = entry.getValue();
                    fieldMap.put(tempKey, Class.forName(tempValue.toString()));
                }
                DeriveData deriveData = new DeriveData<>();
                deriveData.setTableId(tableId);
                deriveData.setFieldMap(fieldMap);
                deriveData.setDerive(derive);
                list.add(deriveData);
            }
            //List<DeriveData> list = JSONUtil.toList(jsonArrayString, DeriveData.class);
            if (CollUtil.isEmpty(list)) {
                return;
            }
            for (DeriveData deriveData : list) {
                initDeriveMetricCalculate(deriveData);
                broadcastState.put(deriveData.getDerive().getId(), deriveData);
            }
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
