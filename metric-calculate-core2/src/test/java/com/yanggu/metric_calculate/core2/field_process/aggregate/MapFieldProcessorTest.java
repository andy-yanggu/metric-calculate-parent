package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MapUdafParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core2.field_process.FieldProcessorTestBase.getMapFieldProcessor;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 映射类型字段处理器单元测试类
 */
class MapFieldProcessorTest {

    private Map<String, Class<?>> fieldMap;

    @BeforeEach
    void init() throws Exception {
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
    void testInit() {
    }

    @Test
    void testProcess() throws Exception {

        //out给in转账, 记录out给每个in转账的总金额
        String jsonString = FileUtil.readUtf8String("test_map_unit_udaf_param.json");
        MapUdafParam mapUdafParam = JSONUtil.toBean(jsonString, MapUdafParam.class);

        MapFieldProcessor<Pair<MultiFieldDistinctKey, Integer>> mapFieldProcessor = getMapFieldProcessor(fieldMap, mapUdafParam);

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "a");
        input1.set("account_no_in", "b");
        input1.set("amount", 1);

        Pair<MultiFieldDistinctKey, Integer> process = mapFieldProcessor.process(input1);
        assertEquals(new MultiFieldDistinctKey(CollUtil.toList("b")), process.getKey());
        assertEquals(Integer.valueOf(1), process.getValue());
    }

}