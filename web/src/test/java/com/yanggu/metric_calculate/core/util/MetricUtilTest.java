package com.yanggu.metric_calculate.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.client.magiccube.pojo.Atom;
import com.yanggu.metric_calculate.client.magiccube.pojo.Composite;
import com.yanggu.metric_calculate.client.magiccube.pojo.Derive;
import com.yanggu.metric_calculate.core.calculate.AtomMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.CompositeMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class MetricUtilTest {

    @Test
    public void testInitAtom() throws Exception {
        AtomMetricCalculate result = MetricUtil.initAtom(new Atom(), new MetricCalculate());
        Assert.assertEquals(new AtomMetricCalculate(), result);
    }

    @Test
    public void testInitDerive() throws Exception {
        DeriveMetricCalculate result = MetricUtil.initDerive(new Derive(), new MetricCalculate());
        Assert.assertEquals(new DeriveMetricCalculate(), result);
    }

    @Test
    public void testInitComposite() throws Exception {
        List<CompositeMetricCalculate> result = MetricUtil.initComposite(new Composite(), new MetricCalculate());
        Assert.assertEquals(Arrays.<CompositeMetricCalculate>asList(new CompositeMetricCalculate()), result);
    }

    @Test
    public void testGetFieldMap() throws Exception {
        Map<String, Class<?>> result = MetricUtil.getFieldMap(new MetricCalculate());
        Assert.assertEquals(new HashMap<String, Class<?>>() {{
            put("String", null);
        }}, result);
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