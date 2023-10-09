package com.yanggu.metric_calculate.jmh_test;


import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;

import java.io.InputStream;

/**
 * 火焰图测试代码
 * <p>找出代码运行的瓶颈</p>
 */
public class JmhTest3 {

    public static void main(String[] args) {
        InputStream resourceAsStream = JmhTest3.class.getClassLoader().getResourceAsStream("mock_metric_config/1.json");
        String jsonString = IoUtil.read(resourceAsStream).toString();
        MetricCalculate tempMetricCalculate = JSONUtil.toBean(jsonString, new TypeReference<MetricCalculate>() {});

        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(tempMetricCalculate);
        DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(1L);

        JSONObject tempInput = new JSONObject();
        tempInput.set("account_no_out", "000000000011");
        tempInput.set("account_no_in", "000000000012");
        tempInput.set("amount", "800");
        tempInput.set("trans_timestamp", "1679887968782");

        tempInput = metricCalculate.getParam(tempInput);

        for (int i = 0; i < 100000000; i++) {
            DeriveMetricCalculateResult<Double> deriveMetricCalculateResults = deriveMetricCalculate.stateExec(tempInput);
        }
    }

}
