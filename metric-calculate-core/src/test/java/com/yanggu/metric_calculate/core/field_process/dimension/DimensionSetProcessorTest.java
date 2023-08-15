package com.yanggu.metric_calculate.core.field_process.dimension;

import com.yanggu.metric_calculate.core.pojo.metric.Dimension;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 维度字段处理器单元测试类
 */
class DimensionSetProcessorTest {

    /**
     * 验证是否按照ColumnIndex进行排序
     */
    @Test
    void testInit() {
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor();
        List<Dimension> dimensionList = new ArrayList<>();
        dimensionSetProcessor.setDimensionList(dimensionList);

        Dimension dimension1 = new Dimension();
        dimension1.setColumnName("name");
        dimension1.setColumnIndex(1);
        dimension1.setDimensionName("dimension_name");
        dimensionList.add(dimension1);

        Dimension dimension2 = new Dimension();
        dimension2.setColumnName("name2");
        dimension2.setColumnIndex(0);
        dimension2.setDimensionName("dimension_name2");

        dimensionList.add(dimension2);

        dimensionSetProcessor.init();

        List<Dimension> list = dimensionSetProcessor.getDimensionList();

        assertEquals(dimension2, list.get(0));
        assertEquals(dimension1, list.get(1));

    }

    /**
     * 测试能否提取出维度数据
     */
    @Test
    void process() {
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor();

        dimensionSetProcessor.setMetricName("metricName");
        dimensionSetProcessor.setKey("1_1");

        Dimension dimension = new Dimension();
        dimension.setColumnName("name");
        dimension.setColumnIndex(0);
        dimension.setDimensionName("dimension_name");
        dimensionSetProcessor.setDimensionList(Collections.singletonList(dimension));
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