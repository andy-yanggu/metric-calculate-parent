package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.client.magiccube.pojo.Dimension;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 维度字段处理器单元测试类
 */
public class DimensionSetProcessorTest {

    /**
     * 验证是否按照ColumnIndex进行排序
     */
    @Test
    public void testInit() {
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
    public void process() {
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor();
        Dimension dimension = new Dimension();
        dimension.setColumnName("name");
        dimension.setColumnIndex(0);
        dimension.setDimensionName("dimension_name");
        dimensionSetProcessor.setDimensionList(Collections.singletonList(dimension));
        dimensionSetProcessor.init();

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("name", "张三");

        DimensionSet process = dimensionSetProcessor.process(jsonObject);
        assertEquals("张三", process.getDimensionMap().get("dimension_name"));
    }

}