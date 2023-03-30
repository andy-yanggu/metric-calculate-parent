package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * 混合型派生指标单元测试类
 */
public class DeriveMetricCalculateMixTest extends DeriveMetricCalculateBase {

    /**
     * 测试混合类型BASEMIX
     */
    @Test
    public void testBaseMix() throws Exception {
        DeriveMetricCalculate<Map<String, Object>, Map<String, Object>, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculate(0L);

        List<DeriveMetricCalculateResult<Double>> query;

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", 800);

        query = deriveMetricCalculate.stateExec(input1);

        Double result = query.get(0).getResult();
        //0 / 800
        assertEquals(0.0D, result, 0.0D);

        JSONObject input2 = input1.clone();
        input2.set("account_no_in", "张三");
        query = deriveMetricCalculate.stateExec(input2);
        result = query.get(0).getResult();
        //800 / 1600
        assertEquals(0.5D, result, 0.0D);

        JSONObject input3 = input1.clone();
        input3.set("account_no_in", "张三");
        query = deriveMetricCalculate.stateExec(input3);
        result = query.get(0).getResult();
        //1600 / 2400
        assertEquals(new BigDecimal("0.66666").doubleValue(), Double.parseDouble(result.toString()), 0.001D);
    }

}
