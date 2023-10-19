package com.yanggu.metric_calculate.core.field_process.dimension;

import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelDimensionColumn;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 维度字段处理器单元测试类
 */
class ModelDimensionColumnSetProcessorTest {

    /**
     * 验证是否按照ColumnIndex进行排序
     */
    @Test
    void testInit() {
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor();
        List<ModelDimensionColumn> modelDimensionColumnList = new ArrayList<>();
        dimensionSetProcessor.setModelDimensionColumnList(modelDimensionColumnList);

        ModelDimensionColumn modelDimensionColumn1 = new ModelDimensionColumn();
        modelDimensionColumn1.setColumnName("name");
        modelDimensionColumn1.setColumnIndex(1);
        modelDimensionColumn1.setDimensionName("dimension_name");
        modelDimensionColumnList.add(modelDimensionColumn1);

        ModelDimensionColumn modelDimensionColumn2 = new ModelDimensionColumn();
        modelDimensionColumn2.setColumnName("name2");
        modelDimensionColumn2.setColumnIndex(0);
        modelDimensionColumn2.setDimensionName("dimension_name2");

        modelDimensionColumnList.add(modelDimensionColumn2);

        dimensionSetProcessor.init();

        List<ModelDimensionColumn> list = dimensionSetProcessor.getModelDimensionColumnList();

        assertEquals(modelDimensionColumn2, list.get(0));
        assertEquals(modelDimensionColumn1, list.get(1));

    }

    /**
     * 测试能否提取出维度数据
     */
    @Test
    void process() {
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor();

        dimensionSetProcessor.setMetricName("metricName");
        dimensionSetProcessor.setKey("1_1");

        ModelDimensionColumn modelDimensionColumn = new ModelDimensionColumn();
        modelDimensionColumn.setColumnName("name");
        modelDimensionColumn.setColumnIndex(0);
        modelDimensionColumn.setDimensionName("dimension_name");
        dimensionSetProcessor.setModelDimensionColumnList(Collections.singletonList(modelDimensionColumn));
        dimensionSetProcessor.init();

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("name", "张三");

        DimensionSet process = dimensionSetProcessor.process(jsonObject);

        //验证维度map、MetricName、Key和realKey()逻辑是否正确
        assertEquals("张三", process.getDimensionMap().get("dimension_name"));
        assertEquals("metricName", process.getMetricName());
        assertEquals("1_1", process.getKey());
        assertEquals("1_1:metricName:张三", process.getRealKey());
    }

}