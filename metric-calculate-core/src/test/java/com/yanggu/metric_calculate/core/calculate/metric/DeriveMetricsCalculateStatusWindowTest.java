package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 状态窗口派生指标单元测试类
 */
class DeriveMetricsCalculateStatusWindowTest {

    private static final MetricCalculate metricCalculate;

    static {
        Model model = JSONUtil.toBean(FileUtil.readUtf8String("device_config.json"), Model.class);
        metricCalculate = MetricUtil.initMetricCalculate(model);
    }

    /**
     * 计算设备当前在线时长或者离线时长
     */
    @Test
    void testBaseMix() throws Exception {
        DeriveMetricCalculate<Long, Map<String, Object>, Double> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(1L);

        JSONObject input = new JSONObject();
        input.set("device_id", "test1");
        input.set("status", 1L);
        input.set("timestamp", 1L);

        DeriveMetricCalculateResult<Double> deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input);
        Double result = deriveMetricCalculateResult.getResult();
        assertEquals(0.0D, result.longValue(), 0.0D);

        JSONObject input2 = input.clone();
        input2.set("timestamp", 10L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input2);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(9.0D, result.longValue(), 0.0D);

        JSONObject input3 = input.clone();
        input3.set("timestamp", 22L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input3);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(21.0D, result.longValue(), 0.0D);

        JSONObject input4 = input.clone();
        input4.set("status", 0L);
        input4.set("timestamp", 25L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input4);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(0.0D, result.longValue(), 0.0D);
        assertEquals(Collections.singletonList(0L), deriveMetricCalculateResult.getStatusList());

        JSONObject input5 = input4.clone();
        input5.set("timestamp", 30L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input5);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(5.0D, result.longValue(), 0.0D);
        assertEquals(Collections.singletonList(0L), deriveMetricCalculateResult.getStatusList());

        JSONObject input6 = input4.clone();
        input6.set("status", 1L);
        input6.set("timestamp", 35L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input6);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(0.0D, result.longValue(), 0.0D);
        assertEquals(Collections.singletonList(1L), deriveMetricCalculateResult.getStatusList());

        JSONObject input7 = input6.clone();
        input7.set("timestamp", 38L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input7);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(3.0D, result.longValue(), 0.0D);
        assertEquals(Collections.singletonList(1L), deriveMetricCalculateResult.getStatusList());

    }

}
