package com.yanggu.metric_calculate.core.calculate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.cube.TimedKVMetricCube;
import com.yanggu.metric_calculate.core.pojo.Derive;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DeriveMetricCalculateTest {

    private DeriveMetricCalculate<?> deriveMetricCalculate;

    @Before
    public void init() {
        MetricCalculate metricCalculate = JSONUtil.toBean(FileUtil.readUtf8String("test2.json"), MetricCalculate.class);
        Map<String, Class<?>> fieldMap = MetricUtil.getFieldMap(metricCalculate);
        metricCalculate.setFieldMap(fieldMap);
        Derive derive = metricCalculate.getDerive().get(0);
        deriveMetricCalculate = MetricUtil.initDerive(derive, metricCalculate);
    }

    @Test
    public void exec() {
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

        TimedKVMetricCube exec;
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

}