package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import org.junit.Test;

/**
 * CEP型派生指标单元测试类
 */
public class DeriveMetricCalculatePatternTest extends DeriveMetricCalculateBase {

    @Test
    public void testCEP() {
        DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(10L);

        JSONObject input = new JSONObject();
        input.set("account_no_out", "000000000011");
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("credit_amt_in", 100);
        input.set("debit_amt_out", 800);
        input.set("trans_date", "20220609");
        DeriveMetricCalculateResult<Double> deriveMetricCalculateResults = deriveMetricCalculate.stateExec(input);
        System.out.println(deriveMetricCalculateResults);
    }

}
