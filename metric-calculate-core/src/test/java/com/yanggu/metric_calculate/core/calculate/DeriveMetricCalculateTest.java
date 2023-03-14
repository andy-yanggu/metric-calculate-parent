package com.yanggu.metric_calculate.core.calculate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
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

        List<DeriveMetricCalculateResult> query;
        
        query = deriveMetricCalculate.updateMetricCube(input);
        System.out.println(query.get(0));

        input.set("debit_amt_out", 900);
        input.set("trans_date", "20220608");
        
        query = deriveMetricCalculate.updateMetricCube(input);
        System.out.println(query.get(0));

        input.set("trans_date", "20220609");
        
        query = deriveMetricCalculate.updateMetricCube(input);
        System.out.println(query.get(0));

        input.set("trans_date", "20220607");
        
        query = deriveMetricCalculate.updateMetricCube(input);
        System.out.println(query.get(0));
    }

    /**
     * 测试集合型SORTEDLISTFIELD
     * 交易金额降序列表, 最多4个
     */
    @Test
    public void test2() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(1);
        List<DeriveMetricCalculateResult> query;

        JSONObject input1 = new JSONObject();
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("debit_amt_out", "800");
        
        query = deriveMetricCalculate.updateMetricCube(input1);
        assertEquals(Collections.singletonList(new BigDecimal("800")), query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.updateMetricCube(input2);
        assertEquals(Arrays.asList(new BigDecimal("900"), new BigDecimal("800")), query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("debit_amt_out", 700);
        query = deriveMetricCalculate.updateMetricCube(input3);
        assertEquals(Arrays.asList(new BigDecimal("900"), new BigDecimal("800"), new BigDecimal("700")),
                query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("debit_amt_out", 600);
        query = deriveMetricCalculate.updateMetricCube(input4);
        assertEquals(Arrays.asList(new BigDecimal("900"), new BigDecimal("800"), new BigDecimal("700"), new BigDecimal("600")), query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_in", "000000000012");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("debit_amt_out", 800);
        query = deriveMetricCalculate.updateMetricCube(input5);
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
        List<DeriveMetricCalculateResult> query;

        JSONObject input1 = new JSONObject();
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("debit_amt_out", "800");
        
        query = deriveMetricCalculate.updateMetricCube(input1);
        assertEquals("1654768045000", query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768046000");
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.updateMetricCube(input2);
        assertEquals("1654768046000", query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("debit_amt_out", 800);
        query = deriveMetricCalculate.updateMetricCube(input3);
        assertEquals("1654768046000", query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1354768045000");
        input4.set("debit_amt_out", 1100);
        query = deriveMetricCalculate.updateMetricCube(input4);
        assertEquals("1354768045000", query.get(0).getResult());
    }

    /**
     * 测试滑动计数窗口类LISTOBJECTCOUNTWINDOW
     * 当天近5比交易贷方金额总和
     */
    @Test
    public void test4() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(3);
        List<DeriveMetricCalculateResult> query;

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", "800");
        
        query = deriveMetricCalculate.updateMetricCube(input1);
        assertEquals(BigDecimal.valueOf(800L), query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.updateMetricCube(input2);
        assertEquals(BigDecimal.valueOf(1700L), query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        query = deriveMetricCalculate.updateMetricCube(input3);
        assertEquals(BigDecimal.valueOf(2700L), query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 1100);
        query = deriveMetricCalculate.updateMetricCube(input4);
        assertEquals(BigDecimal.valueOf(3800L), query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000012");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        query = deriveMetricCalculate.updateMetricCube(input5);
        assertEquals(BigDecimal.valueOf(3900L), query.get(0).getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 100);
        query = deriveMetricCalculate.updateMetricCube(input6);
        assertEquals(BigDecimal.valueOf(3200L), query.get(0).getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000012");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        query = deriveMetricCalculate.updateMetricCube(input7);
        assertEquals(BigDecimal.valueOf(2400L), query.get(0).getResult());
    }

    /**
     * 测试LISTOBJECT类型, limit限制为5, 最多只能存储5个
     */
    @Test
    public void test5() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(4);
        List<DeriveMetricCalculateResult> query;

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", "800");
        
        query = deriveMetricCalculate.updateMetricCube(input1);
        assertEquals(Collections.singletonList(input1), query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.updateMetricCube(input2);
        assertEquals(Arrays.asList(input1, input2), query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        query = deriveMetricCalculate.updateMetricCube(input3);
        assertEquals(Arrays.asList(input1, input2, input3), query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 1100);
        query = deriveMetricCalculate.updateMetricCube(input4);
        assertEquals(Arrays.asList(input1, input2, input3, input4), query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000012");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        query = deriveMetricCalculate.updateMetricCube(input5);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.get(0).getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 100);
        query = deriveMetricCalculate.updateMetricCube(input6);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.get(0).getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000012");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        query = deriveMetricCalculate.updateMetricCube(input7);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.get(0).getResult());
    }

    /**
     * 测试SORTEDLISTOBJECT类型, limit限制为5, 最多只能存储5个, 按照debit_amt_out升序排序
     */
    @Test
    public void test6() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(5);
        List<DeriveMetricCalculateResult> query;

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", "800");
        
        query = deriveMetricCalculate.updateMetricCube(input1);
        assertEquals(Collections.singletonList(new HashMap<>(input1)), query.get(0).getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.updateMetricCube(input2);
        assertEquals(Arrays.asList(new HashMap<>(input1), new HashMap<>(input2)), query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        query = deriveMetricCalculate.updateMetricCube(input3);
        assertEquals(Arrays.asList(new HashMap<>(input1), new HashMap<>(input2), new HashMap<>(input3)), query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 80);
        query = deriveMetricCalculate.updateMetricCube(input4);
        assertEquals(Arrays.asList(new HashMap<>(input4), new HashMap<>(input1), new HashMap<>(input2), new HashMap<>(input3)), query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000012");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        query = deriveMetricCalculate.updateMetricCube(input5);
        assertEquals(Arrays.asList(new HashMap<>(input4), new HashMap<>(input5), new HashMap<>(input1), new HashMap<>(input2), new HashMap<>(input3)), query.get(0).getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 120);
        query = deriveMetricCalculate.updateMetricCube(input6);
        assertEquals(Arrays.asList(new HashMap<>(input4), new HashMap<>(input5), new HashMap<>(input6), new HashMap<>(input1), new HashMap<>(input2)), query.get(0).getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000012");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        query = deriveMetricCalculate.updateMetricCube(input7);
        assertEquals(Arrays.asList(new HashMap<>(input4), new HashMap<>(input5), new HashMap<>(input7), new HashMap<>(input6), new HashMap<>(input1)), query.get(0).getResult());
    }

    /**
     * 测试基本映射BASEMAP
     * 所有交易账号的累计交易金额
     */
    @Test
    public void test7() {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(6);
        List<DeriveMetricCalculateResult> query;

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", "800");
        
        query = deriveMetricCalculate.updateMetricCube(input1);
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
        query = deriveMetricCalculate.updateMetricCube(input2);
        map.put(Collections.singletonList("000000000012"), new BigDecimal("1700"));
        assertEquals(map, query.get(0).getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        query = deriveMetricCalculate.updateMetricCube(input3);
        map.put(Collections.singletonList("000000000012"), new BigDecimal("2700"));
        assertEquals(map, query.get(0).getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000013");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 80);
        query = deriveMetricCalculate.updateMetricCube(input4);
        map.put(Collections.singletonList("000000000013"), new BigDecimal("80"));
        assertEquals(map, query.get(0).getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000013");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        query = deriveMetricCalculate.updateMetricCube(input5);
        map.put(Collections.singletonList("000000000013"), new BigDecimal("180"));
        assertEquals(map, query.get(0).getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 120);
        query = deriveMetricCalculate.updateMetricCube(input6);
        map.put(Collections.singletonList("000000000012"), new BigDecimal("2820"));
        assertEquals(map, query.get(0).getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000016");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        query = deriveMetricCalculate.updateMetricCube(input7);
        map.put(Collections.singletonList("000000000016"), new BigDecimal("100"));
        assertEquals(map, query.get(0).getResult());
    }

    /**
     * 测试混合类型BASEMIX
     */
    @Test
    public void test8() throws Exception {
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(7);
        List<DeriveMetricCalculateResult> query;

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", "800");
        
        query = deriveMetricCalculate.updateMetricCube(input1);

        Object result = query.get(0).getResult();
        //0 / 800
        assertEquals(BigDecimal.ZERO, result);

        JSONObject input2 = input1.clone();
        input2.set("account_no_in", "张三");
        query = deriveMetricCalculate.updateMetricCube(input2);
        result = query.get(0).getResult();
        //800 / 1600
        assertEquals(new BigDecimal("0.5"), result);

        JSONObject input3 = input1.clone();
        input3.set("account_no_in", "张三");
        query = deriveMetricCalculate.updateMetricCube(input3);
        result = query.get(0).getResult();
        //1600 / 2400
        assertEquals(new BigDecimal("0.66666").doubleValue(), Double.parseDouble(result.toString()), 0.001D);
    }

    /**
     * 测试CEP类型的指标
     *
     * @throws Exception
     */
    @Test
    public void test9() throws Exception {
        //CEP链
        //start     debit_amt_out > 100
        //mid       debit_amt_out > 200   不超过10ms(start和mid之间时间差)
        //end       debit_amt_out > 300   不超过10ms(mid和end之间时间差)
        DeriveMetricCalculate<?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(8);
        List<DeriveMetricCalculateResult> query;

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", "800");
        
        query = deriveMetricCalculate.updateMetricCube(input1);
        System.out.println(query);
    }
}