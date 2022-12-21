package com.yanggu.metric_calculate.core.fieldprocess;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 指标的维度类（名称和维度数据）
 */
@Data
@NoArgsConstructor
public class DimensionSet {

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 维度kv
     * k是维度名称, value是维度值
     */
    private Map<String, Object> dimensionMap;

    public DimensionSet(String metricName, Map<String, Object> dimensionMap) {
        this.metricName = metricName;
        this.dimensionMap = dimensionMap;
    }

    /**
     * 获取唯一的key
     * @return
     */
    public String realKey() {
        return metricName + ":" + dimensionMap.values().stream().map(Object::toString).collect(Collectors.joining(":"));
    }

}
