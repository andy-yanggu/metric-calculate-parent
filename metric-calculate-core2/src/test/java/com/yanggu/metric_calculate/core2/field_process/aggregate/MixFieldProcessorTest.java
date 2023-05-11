package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MixUnitUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MixFieldProcessorTest {

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

        MixFieldProcessor<Map<String, Long>> mixFieldProcessor = FieldProcessorUtil.getMixFieldProcessor(fieldMap, mixUnitUdafParam);

        JSONObject input1 = new JSONObject();
        input1.set("amount", 100L);
        input1.set("city", "上海");

        Map<String, Long> process = mixFieldProcessor.process(input1);

        assertEquals(2, process.size());
        assertEquals(100L, process.get("上海_sum").longValue());
        assertEquals(100L, process.get("全国_sum").longValue());

        JSONObject input2 = new JSONObject();
        input2.set("amount", 200L);
        input2.set("city", "北京");
        process = mixFieldProcessor.process(input2);
        assertEquals(2, process.size());
        assertEquals(0L, process.get("上海_sum").longValue());
        assertEquals(200L, process.get("全国_sum").longValue());
    }

}