package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
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

        Map<String, Object> input = new HashMap<>();
        input.put("device_id", "test1");
        input.put("status", 1L);
        input.put("timestamp", 1L);

        DeriveMetricCalculateResult<Double> deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input);
        Double result = deriveMetricCalculateResult.getResult();
        assertEquals(0.0D, result.longValue(), 0.0D);

        Map<String, Object> input2 = new HashMap<>();
        input2.put("device_id", "test1");
        input2.put("status", 1L);
        input2.put("timestamp", 10L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input2);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(9.0D, result.longValue(), 0.0D);

        Map<String, Object> input3 = new HashMap<>();
        input3.put("device_id", "test1");
        input3.put("status", 1L);
        input3.put("timestamp", 22L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input3);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(21.0D, result.longValue(), 0.0D);

        Map<String, Object> input4 = new HashMap<>();
        input4.put("device_id", "test1");
        input4.put("status", 0L);
        input4.put("timestamp", 25L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input4);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(0.0D, result.longValue(), 0.0D);
        assertEquals(Collections.singletonList(0L), deriveMetricCalculateResult.getStatusList());

        Map<String, Object> input5 = new HashMap<>();
        input5.put("device_id", "test1");
        input5.put("status", 0L);
        input5.put("timestamp", 30L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input5);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(5.0D, result.longValue(), 0.0D);
        assertEquals(Collections.singletonList(0L), deriveMetricCalculateResult.getStatusList());

        Map<String, Object> input6 = new HashMap<>();
        input6.put("device_id", "test1");
        input6.put("status", 1L);
        input6.put("timestamp", 35L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input6);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(0.0D, result.longValue(), 0.0D);
        assertEquals(Collections.singletonList(1L), deriveMetricCalculateResult.getStatusList());

        Map<String, Object> input7 = new HashMap<>();
        input7.put("device_id", "test1");
        input7.put("status", 1L);
        input7.put("timestamp", 38L);
        deriveMetricCalculateResult = deriveMetricCalculate.stateExec(input7);
        result = deriveMetricCalculateResult.getResult();
        assertEquals(3.0D, result.longValue(), 0.0D);
        assertEquals(Collections.singletonList(1L), deriveMetricCalculateResult.getStatusList());

    }

}
