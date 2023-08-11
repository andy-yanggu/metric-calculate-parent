package com.yanggu.metric_calculate.core.field_process.dimension;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Objects;
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
    private LinkedHashMap<String, Object> dimensionMap;

    public DimensionSet(String key, String metricName, LinkedHashMap<String, Object> dimensionMap) {
        this.key = key;
        this.metricName = metricName;
        this.dimensionMap = dimensionMap;
    }

    /**
     * 获取唯一的key
     *
     * @return
     */
    public String getRealKey() {
        return key + ":" + metricName + ":" + dimensionMap.values().stream()
                .map(Object::toString)
                .collect(Collectors.joining(":"));
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (dimensionMap != null ? dimensionMap.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DimensionSet that = (DimensionSet) o;

        if (!Objects.equals(key, that.key)) {
            return false;
        }
        return Objects.equals(dimensionMap, that.dimensionMap);
    }

}
