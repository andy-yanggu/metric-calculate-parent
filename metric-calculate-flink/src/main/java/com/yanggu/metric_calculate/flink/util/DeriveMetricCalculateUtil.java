package com.yanggu.metric_calculate.flink.util;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import com.yanggu.metric_calculate.flink.pojo.DeriveConfigData;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.runtime.state.FunctionInitializationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DeriveMetricCalculateUtil {

    private DeriveMetricCalculateUtil() {
    }

    public static final MapStateDescriptor<Long, DeriveConfigData> deriveMapStateDescriptor = new MapStateDescriptor<>("deriveMapState", Long.class, DeriveConfigData.class);

    public static void initializeState(FunctionInitializationContext context, String url) throws Exception {
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

    public static void initDeriveMetricCalculate(DeriveConfigData deriveConfigData) {
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
