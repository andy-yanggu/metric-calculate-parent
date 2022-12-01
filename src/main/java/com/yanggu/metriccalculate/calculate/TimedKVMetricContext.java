package com.yanggu.metriccalculate.calculate;

import lombok.Data;

import java.util.Map;

@Data
public class TimedKVMetricContext implements TaskContext {

    private Map cache;

    @Override
    public Map cache() {
        return cache;
    }

}