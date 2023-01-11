package com.yanggu.metric_calculate.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.pojo.Fields;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * 指标工具类单元测试类
 */
public class MetricUtilTest {

    @Test
    public void testInitAtom() throws Exception {
    }

    @Test
    public void testInitDerive() throws Exception {
    }

    @Test
    public void testInitComposite() throws Exception {
    }

    /**
     * 验证是否校验metricCalculate为空
     *
     * @throws Exception
     */
    @Test
    public void testGetFieldMap1() {
        assertThrows("传入的明细宽表为空", RuntimeException.class, () -> MetricUtil.getFieldMap(null));
    }

    /**
     * 验证是否校验fields为空
     *
     * @throws Exception
     */
    @Test
    public void testGetFieldMap2() {
        MetricCalculate metricCalculate = new MetricCalculate();
        assertThrows("宽表字段为空, 宽表数据: " + JSONUtil.toJsonStr(metricCalculate), RuntimeException.class,
                () -> MetricUtil.getFieldMap(metricCalculate));
    }

    /**
     * 验证方法是否正常执行
     *
     * @throws Exception
     */
    @Test
    public void testGetFieldMap3() {
        MetricCalculate metricCalculate = JSONUtil.toBean(FileUtil.readUtf8String("test2.json"), MetricCalculate.class);
        Map<String, Class<?>> fieldMap = MetricUtil.getFieldMap(metricCalculate);

        List<Fields> fields = metricCalculate.getFields();
        fields.forEach(tempField -> {
            assertTrue(fieldMap.containsKey(tempField.getName()));
            assertTrue(fieldMap.containsValue(tempField.getValueType().getType()));
        });

    }

    /**
     * 测试getParam方法, 测试是否校验input为空
     *
     * @throws Exception
     */
    @Test
    public void testGetParam1() throws Exception {
        assertThrows("输入数据为空", RuntimeException.class, () -> MetricUtil.getParam(null, null));
        assertThrows("宽表字段为空", RuntimeException.class, () -> MetricUtil.getParam(null, null));
    }

    /**
     * 测试是否校验fieldMap
     *
     * @throws Exception
     */
    @Test
    public void testGetParam2() throws Exception {
        JSONObject input = new JSONObject();
        input.set("key", "value");
        assertThrows("宽表字段为空", RuntimeException.class, () -> MetricUtil.getParam(input, null));
    }

    /**
     * 当input中没有数据时, 应该返回null
     *
     * @throws Exception
     */
    @Test
    public void testGetParam3() throws Exception {
        JSONObject input = new JSONObject();
        input.set("key", "value");

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("key1", String.class);

        Map<String, Object> param = MetricUtil.getParam(input, fieldMap);

        assertTrue(CollUtil.isEmpty(param));

    }

}