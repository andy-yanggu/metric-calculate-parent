package com.yanggu.metric_calculate.flink.util;


import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import com.yanggu.metric_calculate.flink.pojo.DeriveConfigData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.json.JSONUtil;

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
            List<DeriveConfigData> list = JSONUtil.toList(jsonArrayString, DeriveConfigData.class);
            if (CollUtil.isEmpty(list)) {
                return;
            }
            for (DeriveConfigData deriveConfigData : list) {
                initDeriveMetricCalculate(deriveConfigData);
                broadcastState.put(deriveConfigData.getDeriveMetrics().getId(), deriveConfigData);
            }
        }
    }

    @SneakyThrows
    public static void initDeriveMetricCalculate(DeriveConfigData deriveConfigData) {
        if (deriveConfigData == null) {
            log.error("传入的deriveData为null");
            return;
        }
        Long tableId = deriveConfigData.getTableId();
        Map<String, Class<?>> fieldMap = deriveConfigData.getFieldMap();
        DeriveMetrics deriveMetrics = deriveConfigData.getDeriveMetrics();
        AviatorFunctionFactory aviatorFunctionFactory = new AviatorFunctionFactory(deriveConfigData.getAviatorFunctionJarPathList());
        aviatorFunctionFactory.init();
        AggregateFunctionFactory aggregateFunctionFactory = new AggregateFunctionFactory(deriveConfigData.getUdafJarPathList());
        aggregateFunctionFactory.init();
        DeriveMetricCalculate deriveMetricCalculate = MetricUtil.initDerive(deriveMetrics, tableId, fieldMap, aviatorFunctionFactory, aggregateFunctionFactory);
        deriveConfigData.setDeriveMetricCalculate(deriveMetricCalculate);
    }

}
