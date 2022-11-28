package com.yanggu.metriccalculate.cube;

import com.yanggu.metriccalculate.number.CubeInteger;
import com.yanggu.metriccalculate.number.CubeLong;
import com.yanggu.metriccalculate.unit.numeric.SumUnit;

import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @author: YangGu
 * @date: 2022/11/25 23:27
 * @description:
 */
public class Test2 {

    public static void main(String[] args) {
        Map<String, TimedKVMetricCube> map = new HashMap<>();

        TimeSeriesKVTable<SumUnit<CubeLong>> table = new TimeSeriesKVTable<>();

        SumUnit<CubeLong> unit = new SumUnit<>(CubeLong.of(100));
        table.put(999L, unit);
        table.put(1000L, unit);
        table.put(1001L, unit);


    }

}
