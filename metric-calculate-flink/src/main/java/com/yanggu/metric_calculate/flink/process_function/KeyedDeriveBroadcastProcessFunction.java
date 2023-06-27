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
import com.yanggu.metric_calculate.flink.pojo.DeriveCalculateData;
import com.yanggu.metric_calculate.flink.pojo.DeriveConfigData;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE_ID;
import static com.yanggu.metric_calculate.flink.util.Constant.HISTORY_METRIC_CUBE;

@Slf4j
public class KeyedDeriveBroadcastProcessFunction extends KeyedBroadcastProcessFunction<DimensionSet, DeriveCalculateData, DeriveConfigData, MetricCube>
                implements CheckpointedFunction, Serializable {

    private static final long serialVersionUID = 6092835299466260638L;

    private String url = "http://localhost:8888/mock-model/all-derive-data";

    private final MapStateDescriptor<Long, DeriveConfigData> deriveMapStateDescriptor = new MapStateDescriptor<>("deriveMapState", Long.class, DeriveConfigData.class);

    @Override
    public void processElement(DeriveCalculateData value,
                               KeyedBroadcastProcessFunction<DimensionSet, DeriveCalculateData, DeriveConfigData, MetricCube>.ReadOnlyContext ctx,
                               Collector<MetricCube> out) throws Exception {
        ReadOnlyBroadcastState<Long, DeriveConfigData> broadcastState = ctx.getBroadcastState(deriveMapStateDescriptor);
        Long deriveId = value.getDeriveId();
        DeriveConfigData deriveConfigData = broadcastState.get(deriveId);
        if (deriveConfigData == null) {
            log.error("广播状态中没有派生指标配置数据: 派生指标id: {}", deriveId);
            return;
        }
        DeriveMetricCalculate deriveMetricCalculate = deriveConfigData.getDeriveMetricCalculate();
        DimensionSet dimensionSet = ctx.getCurrentKey();
        MetricCube historyMetricCube = value.getMetricCube();
        historyMetricCube = deriveMetricCalculate.addInput(value.getData(), historyMetricCube, dimensionSet);
        value.setMetricCube(historyMetricCube);
        DeriveMetricCalculateResult deriveMetricCalculateResult = historyMetricCube.query();
        if (deriveMetricCalculateResult != null) {
            ctx.output(new OutputTag<>("derive-result", TypeInformation.of(DeriveMetricCalculateResult.class)), deriveMetricCalculateResult);
        }
        out.collect(historyMetricCube);
    }

    @Override
    public void processBroadcastElement(DeriveConfigData value,
                                        KeyedBroadcastProcessFunction<DimensionSet, DeriveCalculateData, DeriveConfigData, MetricCube>.Context ctx,
                                        Collector<MetricCube> out) throws Exception {
        initDeriveMetricCalculate(value);
        BroadcastState<Long, DeriveConfigData> broadcastState = ctx.getBroadcastState(deriveMapStateDescriptor);
        broadcastState.put(value.getDerive().getId(), value);
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        BroadcastState<Long, DeriveConfigData> broadcastState = context.getOperatorStateStore().getBroadcastState(deriveMapStateDescriptor);
        if (context.isRestored()) {
            broadcastState.iterator().forEachRemaining(temp -> initDeriveMetricCalculate(temp.getValue()));
        } else {
            //获取所有的派生指标数据
            String jsonArrayString = HttpUtil.get(url);
            if (StrUtil.isBlank(jsonArrayString)) {
                return;
            }
            JSONArray objects = JSONUtil.parseArray(jsonArrayString);
            List<DeriveConfigData> list = new ArrayList<>();
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
                DeriveConfigData deriveConfigData = new DeriveConfigData<>();
                deriveConfigData.setTableId(tableId);
                deriveConfigData.setFieldMap(fieldMap);
                deriveConfigData.setDerive(derive);
                list.add(deriveConfigData);
            }
            //List<DeriveConfigData> list = JSONUtil.toList(jsonArrayString, DeriveConfigData.class);
            if (CollUtil.isEmpty(list)) {
                return;
            }
            for (DeriveConfigData deriveConfigData : list) {
                initDeriveMetricCalculate(deriveConfigData);
                broadcastState.put(deriveConfigData.getDerive().getId(), deriveConfigData);
            }
        }
    }

    private void initDeriveMetricCalculate(DeriveConfigData deriveConfigData) {
        if (deriveConfigData == null) {
            log.error("传入的deriveData为null");
            return;
        }
        Long tableId = deriveConfigData.getTableId();
        Map<String, Class<?>> fieldMap = deriveConfigData.getFieldMap();
        Derive derive = deriveConfigData.getDerive();
        DeriveMetricCalculate deriveMetricCalculate = MetricUtil.initDerive(derive, tableId, fieldMap);
        deriveConfigData.setDeriveMetricCalculate(deriveMetricCalculate);
    }

}
