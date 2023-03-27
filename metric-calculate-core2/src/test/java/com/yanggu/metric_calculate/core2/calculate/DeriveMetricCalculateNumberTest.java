package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.core.lang.mutable.MutableObj;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import org.junit.Test;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * 数值型派生指标单元测试类
 */
public class DeriveMetricCalculateNumberTest extends DeriveMetricCalculateBase {

    /**
     * 测试SUM求和
     */
    @Test
    public void testSum() {
        DeriveMetricCalculate<Integer, Double, Double> deriveMetricCalculate = 
                metricCalculate.getDeriveMetricCalculate(1L);

        JSONObject input = new JSONObject();
        input.set("account_no_out", "000000000011");
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("credit_amt_in", 100);
        input.set("debit_amt_out", 800);
        input.set("trans_date", "20220609");

        List<DeriveMetricCalculateResult<Double>> doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.get(0).getResult(), 0.0D);

        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(1600.0D, doubles.get(0).getResult(), 0.0D);

        input.set("debit_amt_out", 400);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(2000.0D, doubles.get(0).getResult(), 0.0D);
    }

    /**
     * 测试MIN, 最小值
     */
    @Test
    public void testMin() {
        DeriveMetricCalculate<Integer, MutableObj<Integer>, Integer> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculate(2L);

        JSONObject input = new JSONObject();
        input.set("account_no_out", "000000000011");
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("credit_amt_in", 100);
        input.set("debit_amt_out", 800);
        input.set("trans_date", "20220609");

        List<DeriveMetricCalculateResult<Integer>> doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(new Integer(800), doubles.get(0).getResult());

        input.set("debit_amt_out", 900);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(new Integer(800), doubles.get(0).getResult());

        input.set("debit_amt_out", 400);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(new Integer(400), doubles.get(0).getResult());

        input.set("debit_amt_out", 500);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(new Integer(400), doubles.get(0).getResult());
    }

}
