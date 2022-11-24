package com.yanggu.metriccalculate.cube;

import com.yanggu.metriccalculate.fieldprocess.TimeBaselineDimension;
import lombok.Data;

import java.util.Map;
import java.util.stream.Collectors;

@Data
public class TimedKVMetricCube<V> {


    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标维度
     */
    private Map<String, Object> dimensionMap;

    private TimeBaselineDimension timeBaselineDimension;

    private TimeSeriesKVTable<V> table;

    public void putValue(Long key, V v) {
        table.putValue(key, v);
    }

    public String getRealKey() {
        return name + ":" + dimensionMap.values().stream().map(String::valueOf).collect(Collectors.joining());
    }

}
