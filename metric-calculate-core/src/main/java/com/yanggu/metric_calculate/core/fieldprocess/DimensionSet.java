package com.yanggu.metric_calculate.core.fieldprocess;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 指标的维度类（名称和维度数据）
 */
@Data
@NoArgsConstructor
public class DimensionSet implements Serializable {

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 维度kv
     * <p>k是维度名称, value是维度值
     */
    private Map<String, Object> dimensionMap;

    public DimensionSet(String key, String metricName, Map<String, Object> dimensionMap) {
        this.key = key;
        this.metricName = metricName;
        this.dimensionMap = dimensionMap;
    }

    /**
     * 获取唯一的key
     *
     * @return
     */
    public String realKey() {
        return key + ":" + metricName + ":" + dimensionMap.values().stream()
                .map(Object::toString)
                .collect(Collectors.joining(":"));
    }

}
