package com.yanggu.metric_calculate.core.calculate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.cube.Table;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DeriveMetricCalculateTest {

    private MetricCalculate metricCalculate;

    @Before
    public void init() {
        MetricCalculate metricCalculate = JSONUtil.toBean(FileUtil.readUtf8String("test2.json"), MetricCalculate.class);
        Map<String, Class<?>> fieldMap = MetricUtil.getFieldMap(metricCalculate);
        metricCalculate.setFieldMap(fieldMap);
        this.metricCalculate = metricCalculate;
    }

    @Test
    public void test1() {
        DeriveMetricCalculate<?> deriveMetricCalculate = MetricUtil.initDerive(metricCalculate.getDerive().get(0), metricCalculate);
        String jsonString =
                "{\n" +
                        "    \"account_no_out\": \"000000000011\",\n" +
                        "    \"account_no_in\": \"000000000012\",\n" +
                        "    \"trans_timestamp\": \"1654768045000\",\n" +
                        "    \"credit_amt_in\": 100,\n" +
                        "    \"debit_amt_out\": 800,\n" +
                        "    \"trans_date\":\"20220609\"\n" +
                        "}";
        JSONObject input = JSONUtil.parseObj(jsonString);

        MetricCube<Table, Long, ?, ?> exec;
        List<DeriveMetricCalculateResult> query;
        exec = deriveMetricCalculate.exec(input);
        query = deriveMetricCalculate.query(exec);
        System.out.println(query.get(0));

        input.set("debit_amt_out", 900);
        input.set("trans_date", "20220608");
        exec = deriveMetricCalculate.exec(input);
        query = deriveMetricCalculate.query(exec);
        System.out.println(query.get(0));

        input.set("trans_date", "20220609");
        exec = deriveMetricCalculate.exec(input);
        query = deriveMetricCalculate.query(exec);
        System.out.println(query.get(0));

        input.set("trans_date", "20220607");
        exec = deriveMetricCalculate.exec(input);
        query = deriveMetricCalculate.query(exec);
        System.out.println(query.get(0));
    }

    @Test
    public void test2() {
        DeriveMetricCalculate<?> deriveMetricCalculate = MetricUtil.initDerive(metricCalculate.getDerive().get(3), metricCalculate);
        MetricCube<Table, Long, ?, ?> exec;
        List<DeriveMetricCalculateResult> query;

        JSONObject input = new JSONObject();
        input.set("account_no_out", "000000000011");
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("credit_amt_in", "100");
        input.set("trans_date", "20220609");
        input.set("debit_amt_out", "800");
        exec = deriveMetricCalculate.exec(input);
        query = deriveMetricCalculate.query(exec);
        assertEquals(BigDecimal.valueOf(800L), query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        exec = deriveMetricCalculate.exec(input2);
        query = deriveMetricCalculate.query(exec);
        assertEquals(BigDecimal.valueOf(1700L), query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        exec = deriveMetricCalculate.exec(input3);
        query = deriveMetricCalculate.query(exec);
        assertEquals(BigDecimal.valueOf(2700L), query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 1100);
        exec = deriveMetricCalculate.exec(input4);
        query = deriveMetricCalculate.query(exec);
        assertEquals(BigDecimal.valueOf(3800L), query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000012");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        exec = deriveMetricCalculate.exec(input5);
        query = deriveMetricCalculate.query(exec);
        assertEquals(BigDecimal.valueOf(3900L), query.get(0).getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 100);
        exec = deriveMetricCalculate.exec(input6);
        query = deriveMetricCalculate.query(exec);
        assertEquals(BigDecimal.valueOf(3200L), query.get(0).getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000012");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        exec = deriveMetricCalculate.exec(input7);
        query = deriveMetricCalculate.query(exec);
        assertEquals(BigDecimal.valueOf(2400L), query.get(0).getResult());
    }

}