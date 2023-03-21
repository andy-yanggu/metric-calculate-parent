package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import org.junit.Before;
import org.junit.Test;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class DeriveMetricCalculateTest {

    private MetricCalculate metricCalculate;

    @Before
    public void init() {
        String jsonString = FileUtil.readUtf8String("test3.json");
        MetricCalculate tempMetricCalculate = JSONUtil.toBean(jsonString, MetricCalculate.class);
        this.metricCalculate = MetricUtil.initMetricCalculate(tempMetricCalculate);
    }

    @Test
    public void testExec1() {
        DeriveMetricCalculate<Integer, Double, Double> deriveMetricCalculate =
                this.metricCalculate.getIndexDeriveMetricCalculate(0);

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
     * 测试LISTOBJECT类型, limit限制为5, 最多只能存储5个
     */
    @Test
    public void test5() {
        DeriveMetricCalculate<JSONObject, List<JSONObject>, List<JSONObject>> deriveMetricCalculate =
                this.metricCalculate.getIndexDeriveMetricCalculate(4);

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", "800");

        List<DeriveMetricCalculateResult<List<JSONObject>>> query = deriveMetricCalculate.stateExec(input1);
        assertEquals(Collections.singletonList(input1), query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals(Arrays.asList(input1, input2), query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals(Arrays.asList(input1, input2, input3), query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 1100);
        query = deriveMetricCalculate.stateExec(input4);
        assertEquals(Arrays.asList(input1, input2, input3, input4), query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000012");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input5);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.get(0).getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input6);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.get(0).getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000012");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input7);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.get(0).getResult());
    }

    /**
     * 测试基本映射BASEMAP
     * 所有交易账号的累计交易金额
     */
    @Test
    public void test7() {
        DeriveMetricCalculate<Tuple2<MultiFieldDistinctKey, Integer>, Map<MultiFieldDistinctKey, Double>,
                Map<MultiFieldDistinctKey, Double>> deriveMetricCalculate
                = this.metricCalculate.getIndexDeriveMetricCalculate(6);

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", 800);

        List<DeriveMetricCalculateResult<Map<MultiFieldDistinctKey, Double>>> query =
                deriveMetricCalculate.stateExec(input1);
        Map<MultiFieldDistinctKey, Double> map = new HashMap<>();
        MultiFieldDistinctKey key = new MultiFieldDistinctKey(Collections.singletonList("000000000012"));
        map.put(key, 800.0D);
        assertEquals(map, query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.stateExec(input2);
        map.put(key, 1700.0D);
        assertEquals(map, query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        query = deriveMetricCalculate.stateExec(input3);
        map.put(key, 2700.0D);
        assertEquals(map, query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000013");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 80);
        query = deriveMetricCalculate.stateExec(input4);

        MultiFieldDistinctKey key2 = new MultiFieldDistinctKey(Collections.singletonList("000000000013"));
        map.put(key2, 80.0D);
        assertEquals(map, query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000013");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input5);
        map.put(key2, 180.0D);
        assertEquals(map, query.get(0).getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 120);
        query = deriveMetricCalculate.stateExec(input6);
        map.put(key, 2820.0D);
        assertEquals(map, query.get(0).getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000016");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input7);
        map.put(new MultiFieldDistinctKey(Collections.singletonList("000000000016")), 100.0D);
        assertEquals(map, query.get(0).getResult());
    }

    /**
     * 测试混合类型BASEMIX
     */
    @Test
    public void test8() throws Exception {
        DeriveMetricCalculate<Map<String, Object>, Map<String, Object>, Double> deriveMetricCalculate =
                this.metricCalculate.getIndexDeriveMetricCalculate(7);

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
