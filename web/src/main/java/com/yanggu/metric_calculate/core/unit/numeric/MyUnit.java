package com.yanggu.metric_calculate.core.unit.numeric;

import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import lombok.experimental.FieldNameConstants;

import java.util.*;


@FieldNameConstants
public class MyUnit<N extends CubeNumber<N>> extends NumberUnit<N, MyUnit<N>> {

    private final TreeMap<Integer, Long> treeMap = new TreeMap<>();

    private double ratio;

    private Integer continueHour;

    public MyUnit(CubeNumber<N> value, Map<String, Object> params) {
        treeMap.put(Integer.parseInt(DateUtil.format(new Date(value.value().longValue()), "HH")), 1L);
        this.ratio = Double.parseDouble(params.get(Fields.ratio).toString());
        this.continueHour = Integer.parseInt(params.get(Fields.continueHour).toString());
    }

    @Override
    public MyUnit<N> merge(MyUnit<N> that) {
        Map<Integer, Long> thisMap = this.treeMap;
        that.treeMap.forEach((k, v) -> thisMap.merge(k, v, Long::sum));
        this.ratio = that.ratio;
        this.continueHour = that.continueHour;
        return this;
    }

    @Override
    public MyUnit<N> fastClone() {
        return null;
    }

    @Override
    public Number value() {
        Map<Integer, Integer> rangMap = new HashMap<>();
        for (Integer key : treeMap.keySet()) {
            int end = key + continueHour;
            rangMap.put(key, end);
        }

        long total = treeMap.values().stream().mapToLong(Long::longValue).sum();
        for (Map.Entry<Integer, Integer> entry : rangMap.entrySet()) {
            SortedMap<Integer, Long> subMap = treeMap.subMap(entry.getKey(), entry.getValue());
            double tempRatio = 1.0 * subMap.values().stream().mapToLong(Long::longValue).sum() / total;
            if (tempRatio >= ratio) {
                return 1;
            }
        }
        return 0;
    }

}
