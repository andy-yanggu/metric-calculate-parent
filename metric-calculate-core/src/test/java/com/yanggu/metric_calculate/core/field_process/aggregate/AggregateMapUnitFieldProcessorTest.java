package com.yanggu.metric_calculate.core.field_process.aggregate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MapUnitUdafParam;
import com.yanggu.metric_calculate.core.unit.map.BaseMapUnit;
import com.yanggu.metric_calculate.core.unit.numeric.SumUnit;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.unit.UnitFactoryTest.getTestUnitFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 映射类型字段处理器单元测试类
 */
public class AggregateMapUnitFieldProcessorTest {

    private Map<String, Class<?>> fieldMap;

    @Before
    public void init() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("account_no_out", String.class);
        fieldMap.put("account_no_in", String.class);
        fieldMap.put("amount", Integer.class);
        this.fieldMap = fieldMap;
    }

    /**
     * 测试init方法
     */
    @Test
    public void testInit() {
    }

    @Test
    public void testProcess1() throws Exception {

        //out给in转账, 记录out给每个in转账的总金额
        String jsonString = FileUtil.readUtf8String("test_map_unit_udaf_param.json");
        MapUnitUdafParam mapUnitUdafParam = JSONUtil.toBean(jsonString, MapUnitUdafParam.class);

        AggregateMapUnitFieldProcessor<JSONObject, BaseMapUnit<MultiFieldDistinctKey, SumUnit<CubeLong>>> processor =
                        FieldProcessorUtil.getAggregateMapUnitFieldProcessor(mapUnitUdafParam, fieldMap, getTestUnitFactory());

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "a");
        input1.set("account_no_in", "b");
        input1.set("amount", 1);

        BaseMapUnit<MultiFieldDistinctKey, SumUnit<CubeLong>> process = processor.process(input1);
        assertNotNull(process);
        Map<MultiFieldDistinctKey, SumUnit<CubeLong>> map = process.getMap();
        assertNotNull(map);
        assertEquals(1, map.size());

        MultiFieldDistinctKey multiFieldDistinctKey = new MultiFieldDistinctKey();
        multiFieldDistinctKey.setFieldList(Collections.singletonList("b"));

        assertEquals(new SumUnit<>(CubeLong.of(1L)), map.get(multiFieldDistinctKey));
    }

}