package com.yanggu.metriccalculate.fieldprocess;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.client.magiccube.pojo.Dimension;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 从原始数据中提取出维度数据
 */
@Data
@Slf4j
@NoArgsConstructor
public class DimensionSetProcessor implements FieldExtractProcessor<JSONObject, DimensionSet> {

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 维度字段
     */
    private List<Dimension> dimensionList;

    public DimensionSetProcessor(List<Dimension> dimensionList) {
        this.dimensionList = dimensionList;
    }

    @Override
    public void init() {
        if (CollUtil.isNotEmpty(dimensionList)) {
            //按照ColumnIndex进行升序排序
            this.dimensionList = dimensionList.stream()
                    .sorted(Comparator.comparingInt(Dimension::getColumnIndex))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public DimensionSet process(JSONObject input) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        if (CollUtil.isNotEmpty(dimensionList)) {
            for (Dimension dimension : dimensionList) {
                Object result = input.get(dimension.getColumnName());
                if (result == null) {
                    throw new RuntimeException("没有对应的维度值, 字段名称: "
                            + dimension.getColumnName() + ", 原始数据: " + JSONUtil.toJsonStr(input));
                }
                map.put(dimension.getDimensionName(), result);
            }
        }

        return new DimensionSet(metricName, map);
    }

}
