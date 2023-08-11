package com.yanggu.metric_calculate.jmh_test;


import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.MetricUtil;

import java.io.InputStream;

/**
 * 火焰图测试代码
 * <p>找出代码运行的瓶颈</p>
 */
public class JmhTest3 {

    public static void main(String[] args) {
        InputStream resourceAsStream = JmhTest2.class.getClassLoader().getResourceAsStream("metric_config.json");
        String jsonString = IoUtil.read(resourceAsStream).toString();
        MetricCalculate tempMetricCalculate = JSONUtil.toBean(jsonString, new TypeReference<MetricCalculate>() {
        }, true);

        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(tempMetricCalculate);
        DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(1L);

        JSONObject tempInput = new JSONObject();
        tempInput.set("account_no_out", "000000000011");
        tempInput.set("account_no_in", "000000000012");
        tempInput.set("trans_timestamp", "1654768045000");
        tempInput.set("credit_amt_in", "100");
        tempInput.set("debit_amt_out", "800");
        tempInput.set("trans_timestamp", "1679887968782");

        tempInput = metricCalculate.getParam(tempInput);

        for (int i = 0; i < 100000000; i++) {
            DeriveMetricCalculateResult<Double> deriveMetricCalculateResults = deriveMetricCalculate.stateExec(tempInput);
        }
    }

}
