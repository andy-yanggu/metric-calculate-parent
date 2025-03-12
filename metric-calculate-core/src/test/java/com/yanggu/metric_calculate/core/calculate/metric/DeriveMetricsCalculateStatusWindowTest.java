package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 状态窗口派生指标单元测试类
 */
@DisplayName("状态窗口派生指标单元测试类")
class DeriveMetricsCalculateStatusWindowTest {

    private static DeriveMetricCalculate<Long, Map<String, Object>, BigDecimal> deriveMetricCalculate;

    @BeforeAll
    static void init() {
        Model model = JSONUtil.toBean(FileUtil.readUtf8String("device_config.json"), Model.class);
        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(model);
        deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(1L);
    }

    /**
     * 计算设备当前在线时长或者离线时长
     * <p>当状态改变时，1-上线，0-下线。计算在线或者离线时长</p>
     */
    @ParameterizedTest
    @DisplayName("计算设备当前在线时长或者离线时长")
    @CsvSource({"1,1,0.0", "1,10,9.0", "1,22,21.0", "0,25,0", "0,30,5.0", "1,35,0.0", "1,38,3.0"})
    void testBaseMix(Long status, Long timestamp, Double expected) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("device_id", "test1");
        paramMap.put("status", status);
        paramMap.put("timestamp", timestamp);

        DeriveMetricCalculateResult<BigDecimal> deriveMetricCalculateResult = deriveMetricCalculate.stateExec(paramMap);
        assertNotNull(deriveMetricCalculateResult);
        BigDecimal result = deriveMetricCalculateResult.getResult();
        assertEquals(expected, result.doubleValue(), 0.0D);
        assertEquals(List.of(status), deriveMetricCalculateResult.getStatusList());
    }

}
