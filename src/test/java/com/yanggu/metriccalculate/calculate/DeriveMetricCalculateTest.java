package com.yanggu.metriccalculate.calculate;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.client.magiccube.pojo.Derive;
import com.yanggu.metriccalculate.cube.TimedKVMetricCube;
import com.yanggu.metriccalculate.unit.numeric.SumUnit;
import com.yanggu.metriccalculate.util.MetricUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DeriveMetricCalculateTest {

    private DeriveMetricCalculate<?> deriveMetricCalculate;

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
        extracted(input);

        input.set("debit_amt_out", 900);
        input.set("trans_date", "20220608");
        extracted(input);

        input.set("trans_date", "20220609");
        extracted(input);

        input.set("trans_date", "20220607");
        extracted(input);

    }

    private void extracted(JSONObject input) {
        TimedKVMetricCube exec = deriveMetricCalculate.exec(input);
        Object value = exec.query().value();
        Tuple timeWindow = exec.getTimeWindow();
        Object windowStart = timeWindow.get(0);
        Object windowEnd = timeWindow.get(1);

        String collect = exec.getDimensionSet().getDimensionMap().entrySet().stream()
                .map(tempEntry -> tempEntry.getKey() + ":" + tempEntry.getValue())
                .collect(Collectors.joining(","));

        System.out.println("指标名称: " + exec.getName() +
                ", 指标维度: " + collect +
                ", 窗口开始时间: " + DateUtil.formatDateTime(new Date(Long.parseLong(windowStart.toString()))) +
                ", 窗口结束时间: " + DateUtil.formatDateTime(new Date(Long.parseLong(windowEnd.toString()))) +
                ", 聚合值: " + value);
    }

}