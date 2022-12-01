package com.yanggu.metriccalculate.calculate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.client.magiccube.pojo.Derive;
import com.yanggu.metriccalculate.cube.TimeSeriesKVTable;
import com.yanggu.metriccalculate.cube.TimedKVMetricCube;
import com.yanggu.metriccalculate.util.MetricUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeriveMetricCalculateTest {

    private DeriveMetricCalculate deriveMetricCalculate;

    @Before
    public void init() {
        MetricCalculate metricCalculate = JSONUtil.toBean(FileUtil.readUtf8String("test2.json"), MetricCalculate.class);
        Map<String, Class<?>> fieldMap = MetricUtil.getFieldMap(metricCalculate);
        Derive derive = JSONUtil.toBean(FileUtil.readUtf8String("derive.json"), Derive.class);
        deriveMetricCalculate = MetricUtil.initDerive(derive, fieldMap);
        TimedKVMetricContext timedKVMetricContext = new TimedKVMetricContext();
        timedKVMetricContext.setCache(new ConcurrentHashMap());

        deriveMetricCalculate.init(timedKVMetricContext);
    }

    @Test
    public void exec() {
        String jsonString = "{\n" +
                "    \"account_no_out\": \"000000000011\",\n" +
                "    \"account_no_in\": \"000000000012\",\n" +
                "    \"trans_timestamp\": \"1654768045000\",\n" +
                "    \"credit_amt_in\": 100,\n" +
                "    \"debit_amt_out\": 800,\n" +
                "    \"trans_date\":\"20220609\"\n" +
                "}";
        JSONObject input = JSONUtil.parseObj(jsonString);
        TimedKVMetricCube exec = deriveMetricCalculate.exec(input);
        System.out.println(exec);

        input.set("debit_amt_out", 900);
        exec = deriveMetricCalculate.exec(input);

        System.out.println(exec);
    }

}