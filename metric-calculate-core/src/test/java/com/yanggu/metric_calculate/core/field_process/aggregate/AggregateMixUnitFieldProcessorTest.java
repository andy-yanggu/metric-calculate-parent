package com.yanggu.metric_calculate.core.field_process.aggregate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUnitUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.mix.BaseMixedUnit;
import com.yanggu.metric_calculate.core.unit.numeric.SumUnit;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.unit.UnitFactoryTest.getTestUnitFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AggregateMixUnitFieldProcessorTest {

    private Map<String, Class<?>> fieldMap;

    @Before
    public void init() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("city", String.class);
        this.fieldMap = fieldMap;
    }

    @Test
    public void testInit() {
    }

    @Test
    public void process() throws Exception {

        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUnitUdafParam mixUnitUdafParam = JSONUtil.toBean(jsonString, MixUnitUdafParam.class);

        AggregateMixUnitFieldProcessor<JSONObject, BaseMixedUnit> mixUnitFieldProcessor =
                        FieldProcessorUtil.getAggregateMixUnitFieldProcessor(mixUnitUdafParam, fieldMap, getTestUnitFactory());
        mixUnitFieldProcessor.setMixUnitUdafParam(mixUnitUdafParam);

        mixUnitFieldProcessor.init();

        JSONObject input1 = new JSONObject();
        input1.set("amount", 100);
        input1.set("city", "上海");

        BaseMixedUnit process = mixUnitFieldProcessor.process(input1);
        assertNotNull(process);

        Map<String, MergedUnit<?>> dataMap = process.getDataMap();
        assertEquals(2, dataMap.size());
        assertEquals(new SumUnit<>(CubeDecimal.of("100.0"), 1L), dataMap.get("上海_sum"));
        assertEquals(new SumUnit<>(CubeDecimal.of("100.0"), 1L), dataMap.get("全国_sum"));

        Map<String, Object> valueMap = process.value();
        assertEquals(new BigDecimal("100.0"), valueMap.get("上海_sum"));
        assertEquals(new BigDecimal("100.0"), valueMap.get("全国_sum"));

        Object execute = mixUnitFieldProcessor.getExpression().execute(valueMap);
        assertEquals(new BigDecimal(1), execute);

        JSONObject input2 = new JSONObject();
        input2.set("amount", 200);
        input2.set("city", "北京");
        BaseMixedUnit process2 = mixUnitFieldProcessor.process(input2);
        Map<String, Object> valueMap2 = process2.value();
        assertEquals(2, valueMap2.size());
        assertEquals(0L, valueMap2.get("上海_sum"));
        assertEquals(new BigDecimal("200.0"), valueMap2.get("全国_sum"));

        process.merge(process2);
        Map<String, Object> valueMap3 = process.value();

        assertEquals(2, valueMap3.size());
        assertEquals(new BigDecimal("100.0"), valueMap3.get("上海_sum"));
        assertEquals(new BigDecimal("300.0"), valueMap3.get("全国_sum"));

        Object execute2 = mixUnitFieldProcessor.getExpression().execute(valueMap3);
        assertEquals(100.0 / 300.0, new BigDecimal(execute2.toString()).doubleValue(), 0.001D);
    }
}