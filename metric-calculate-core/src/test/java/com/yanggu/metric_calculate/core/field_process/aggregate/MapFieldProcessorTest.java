package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MapUdafParam;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getMapFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 映射类型字段处理器单元测试类
 */
@DisplayName("映射类型字段处理器单元测试类")
class MapFieldProcessorTest {

    private static MapFieldProcessor<Pair<MultiFieldData, Double>> mapFieldProcessor;

    @BeforeAll
    static void init() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("account_no_out", String.class);
        fieldMap.put("account_no_in", String.class);
        fieldMap.put("amount", Double.class);

        String jsonString = FileUtil.readUtf8String("test_map_unit_udaf_param.json");
        MapUdafParam mapUdafParam = JSONUtil.toBean(jsonString, MapUdafParam.class);
        mapFieldProcessor = getMapFieldProcessor(fieldMap, mapUdafParam);
    }

    /**
     * 测试init方法
     */
    @Test
    void testInit() {
    }

    /**
     * 测试process方法
     */
    @ParameterizedTest
    @CsvSource({"'a','b',1", "'c','a',2", "'b','d',3"})
    @DisplayName("测试process方法")
    void testProcess(String accountNoOut, String accountNoIn, Double amount) throws Exception {
        //out给in转账, 记录out给每个in转账的总金额
        //提取出in和金额
        Map<String, Object> input = new HashMap<>();
        input.put("account_no_out", accountNoOut);
        input.put("account_no_in", accountNoIn);
        input.put("amount", amount);

        Pair<MultiFieldData, Double> process = mapFieldProcessor.process(input);
        assertEquals(List.of(accountNoIn), process.getLeft().getFieldList());
        assertEquals(amount, process.getRight(), 0.0D);
    }

}