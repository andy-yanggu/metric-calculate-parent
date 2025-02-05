package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MapUdafParam;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getMapFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 映射类型字段处理器单元测试类
 */
class MapFieldProcessorTest {

    private Map<String, Class<?>> fieldMap;

    @BeforeEach
    void init() throws Exception {
        this.fieldMap = new HashMap<>();
        fieldMap.put("account_no_out", String.class);
        fieldMap.put("account_no_in", String.class);
        fieldMap.put("amount", Integer.class);
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
        MapFieldProcessor<Pair<MultiFieldData, Integer>> mapFieldProcessor = getMapFieldProcessor(fieldMap, mapUdafParam);

        Map<String, Object> input = new HashMap<>();
        input.put("account_no_out", "a");
        input.put("account_no_in", "b");
        input.put("amount", 1);
        Pair<MultiFieldData, Integer> process = mapFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of("b")), process.getLeft());
        assertEquals(Integer.valueOf(1), process.getRight());
    }

}