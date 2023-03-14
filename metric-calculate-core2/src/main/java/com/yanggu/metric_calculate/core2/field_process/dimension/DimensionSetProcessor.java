package com.yanggu.metric_calculate.core2.field_process.dimension;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.Dimension;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 维度字段处理器，从原始数据中提取出维度数据
 */
@Data
@Slf4j
@NoArgsConstructor
public class DimensionSetProcessor<T> implements FieldProcessor<T, DimensionSet> {

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 维度字段
     */
    private List<Dimension> dimensionList;

    /**
     * 宽表字段
     */
    private Map<String, Class<?>> fieldMap;

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
    public DimensionSet process(T input) {
        JSONObject input2 = JSONUtil.parseObj(input);
        Map<String, Object> map = new LinkedHashMap<>();
        if (CollUtil.isNotEmpty(dimensionList)) {
            for (Dimension dimension : dimensionList) {
                Object result = input2.get(dimension.getColumnName());
                if (result == null) {
                    throw new RuntimeException("没有对应的维度值, 字段名称: "
                            + dimension.getColumnName() + ", 原始数据: " + JSONUtil.toJsonStr(input));
                }
                Class<?> clazz = fieldMap.get(dimension.getColumnName());
                map.put(dimension.getDimensionName(), Convert.convert(clazz, result));
            }
        }

        return new DimensionSet(key, metricName, map);
    }

}
