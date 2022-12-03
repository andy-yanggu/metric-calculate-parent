package com.yanggu.metric_calculate.core.calculate;

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