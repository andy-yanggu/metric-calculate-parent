package com.yanggu.metric_calculate.core.calculate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.pojo.DataDetailsWideTable;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * 派生指标计算类单元测试类
 */
public class DeriveMetricCalculateTest {

    private MetricCalculate metricCalculate;

    @Before
    public void init() {
        DataDetailsWideTable table =
                JSONUtil.toBean(FileUtil.readUtf8String("test3.json"), DataDetailsWideTable.class);
        this.metricCalculate = MetricUtil.initMetricCalculate(table);
    }

    /**
     * 测试数值型SUM
     */
    @Test
    public void test1() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(0);
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

    /**
     * 测试集合型SORTEDLISTFIELD
     * 交易金额降序列表, 最多4个
     */
    @Test
    public void test2() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(1);
        MetricCube<Table, Long, ?, ?> exec;
        List<DeriveMetricCalculateResult> query;

        JSONObject input = new JSONObject();
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("debit_amt_out", "800");
        exec = deriveMetricCalculate.exec(input);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Collections.singletonList(new BigDecimal("800")), query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("debit_amt_out", 900);
        exec = deriveMetricCalculate.exec(input2);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(new BigDecimal("900"), new BigDecimal("800")), query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("debit_amt_out", 700);
        exec = deriveMetricCalculate.exec(input3);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(new BigDecimal("900"), new BigDecimal("800"), new BigDecimal("700")),
                query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("debit_amt_out", 600);
        exec = deriveMetricCalculate.exec(input4);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(new BigDecimal("900"), new BigDecimal("800"), new BigDecimal("700"), new BigDecimal("600")), query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_in", "000000000012");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("debit_amt_out", 800);
        exec = deriveMetricCalculate.exec(input5);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(new BigDecimal("900"), new BigDecimal("800"),
                new BigDecimal("800"), new BigDecimal("700")), query.get(0).getResult());

    }

    /**
     * 测试对象型MAXFIELD
     * 最大交易的金额的交易时间戳
     */
    @Test
    public void test3() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(2);
        MetricCube<Table, Long, ?, ?> exec;
        List<DeriveMetricCalculateResult> query;

        JSONObject input = new JSONObject();
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("debit_amt_out", "800");
        exec = deriveMetricCalculate.exec(input);
        query = deriveMetricCalculate.query(exec);
        assertEquals("1654768045000", query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768046000");
        input2.set("debit_amt_out", 900);
        exec = deriveMetricCalculate.exec(input2);
        query = deriveMetricCalculate.query(exec);
        assertEquals("1654768046000", query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("debit_amt_out", 800);
        exec = deriveMetricCalculate.exec(input3);
        query = deriveMetricCalculate.query(exec);
        assertEquals("1654768046000", query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1354768045000");
        input4.set("debit_amt_out", 1100);
        exec = deriveMetricCalculate.exec(input4);
        query = deriveMetricCalculate.query(exec);
        assertEquals("1354768045000", query.get(0).getResult());
    }

    /**
     * 测试滑动计数窗口类LISTOBJECTCOUNTWINDOW
     * 当天近5比交易贷方金额总和
     */
    @Test
    public void test4() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(3);
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

    /**
     * 测试LISTOBJECT类型, limit限制为5, 最多只能存储5个
     */
    @Test
    public void test5() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(4);
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
        assertEquals(Collections.singletonList(input), query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        exec = deriveMetricCalculate.exec(input2);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(input, input2), query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        exec = deriveMetricCalculate.exec(input3);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(input, input2, input3), query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 1100);
        exec = deriveMetricCalculate.exec(input4);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(input, input2, input3, input4), query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000012");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        exec = deriveMetricCalculate.exec(input5);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(input, input2, input3, input4, input5), query.get(0).getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 100);
        exec = deriveMetricCalculate.exec(input6);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(input, input2, input3, input4, input5), query.get(0).getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000012");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        exec = deriveMetricCalculate.exec(input7);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(input, input2, input3, input4, input5), query.get(0).getResult());
    }

    /**
     * 测试SORTEDLISTOBJECT类型, limit限制为5, 最多只能存储5个, 按照debit_amt_out升序排序
     */
    @Test
    public void test6() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(5);
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
        assertEquals(Collections.singletonList(new HashMap<>(input)), query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        exec = deriveMetricCalculate.exec(input2);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(new HashMap<>(input), new HashMap<>(input2)), query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        exec = deriveMetricCalculate.exec(input3);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(new HashMap<>(input), new HashMap<>(input2), new HashMap<>(input3)), query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 80);
        exec = deriveMetricCalculate.exec(input4);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(new HashMap<>(input4), new HashMap<>(input), new HashMap<>(input2), new HashMap<>(input3)), query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000012");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        exec = deriveMetricCalculate.exec(input5);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(new HashMap<>(input4), new HashMap<>(input5), new HashMap<>(input), new HashMap<>(input2), new HashMap<>(input3)), query.get(0).getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 120);
        exec = deriveMetricCalculate.exec(input6);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(new HashMap<>(input4), new HashMap<>(input5), new HashMap<>(input6), new HashMap<>(input), new HashMap<>(input2)), query.get(0).getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000012");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        exec = deriveMetricCalculate.exec(input7);
        query = deriveMetricCalculate.query(exec);
        assertEquals(Arrays.asList(new HashMap<>(input4), new HashMap<>(input5), new HashMap<>(input7), new HashMap<>(input6), new HashMap<>(input)), query.get(0).getResult());
    }

    /**
     * 测试基本映射BASEMAP
     * 所有交易账号的累计交易金额
     */
    @Test
    public void test7() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(6);
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
        Map<List<String>, BigDecimal> map = new HashMap<>();
        map.put(Collections.singletonList("000000000012"), new BigDecimal("800"));
        assertEquals(map, query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        exec = deriveMetricCalculate.exec(input2);
        query = deriveMetricCalculate.query(exec);
        map.put(Collections.singletonList("000000000012"), new BigDecimal("1700"));
        assertEquals(map, query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        exec = deriveMetricCalculate.exec(input3);
        query = deriveMetricCalculate.query(exec);
        map.put(Collections.singletonList("000000000012"), new BigDecimal("2700"));
        assertEquals(map, query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000013");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 80);
        exec = deriveMetricCalculate.exec(input4);
        query = deriveMetricCalculate.query(exec);
        map.put(Collections.singletonList("000000000013"), new BigDecimal("80"));
        assertEquals(map, query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000013");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        exec = deriveMetricCalculate.exec(input5);
        query = deriveMetricCalculate.query(exec);
        map.put(Collections.singletonList("000000000013"), new BigDecimal("180"));
        assertEquals(map, query.get(0).getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 120);
        exec = deriveMetricCalculate.exec(input6);
        query = deriveMetricCalculate.query(exec);
        map.put(Collections.singletonList("000000000012"), new BigDecimal("2820"));
        assertEquals(map, query.get(0).getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000016");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        exec = deriveMetricCalculate.exec(input7);
        query = deriveMetricCalculate.query(exec);
        map.put(Collections.singletonList("000000000016"), new BigDecimal("100"));
        assertEquals(map, query.get(0).getResult());
    }

    /**
     * 测试混合类型BASEMIX
     */
    @Test
    public void test8() throws Exception {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(7);
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

        Object result = query.get(0).getResult();
        //0 / 800
        assertEquals(BigDecimal.ZERO, result);

        JSONObject input2 = input.clone();
        input2.set("account_no_in", "张三");
        exec = deriveMetricCalculate.exec(input2);
        query = deriveMetricCalculate.query(exec);
        result = query.get(0).getResult();
        //800 / 1600
        assertEquals(new BigDecimal("0.5"), result);

        JSONObject input3 = input.clone();
        input3.set("account_no_in", "张三");
        exec = deriveMetricCalculate.exec(input3);
        query = deriveMetricCalculate.query(exec);
        result = query.get(0).getResult();
        //1600 / 2400
        assertEquals(new BigDecimal("0.66666").doubleValue(), Double.parseDouble(result.toString()), 0.001D);
    }

}
