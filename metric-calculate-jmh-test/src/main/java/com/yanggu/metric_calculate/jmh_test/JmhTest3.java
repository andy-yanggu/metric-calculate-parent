package com.yanggu.metric_calculate.jmh_test;


import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 火焰图测试代码
 * <p>找出代码运行的瓶颈</p>
 */
public class JmhTest3 {

    public static void main(String[] args) {
        String jsonString = ResourceUtil.readUtf8Str("mock_metric_config/1.json");
        MetricCalculate tempMetricCalculate = JSONUtil.toBean(jsonString, MetricCalculate.class);

        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(tempMetricCalculate);
        DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(1L);

        Map<String, Object> tempInput = new HashMap<>();
        tempInput.put("account_no_out", "000000000011");
        tempInput.put("account_no_in", "000000000012");
        tempInput.put("amount", "800");
        tempInput.put("trans_timestamp", 1679887968782L);

        tempInput = metricCalculate.getParam(tempInput);

        for (int i = 0; i < 100000000; i++) {
            DeriveMetricCalculateResult<Double> deriveMetricCalculateResults = deriveMetricCalculate.stateExec(tempInput);
        }
    }

}
