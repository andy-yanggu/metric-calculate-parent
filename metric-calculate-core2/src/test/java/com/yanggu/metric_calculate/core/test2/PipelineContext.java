package com.yanggu.metric_calculate.core.test2;

import lombok.Data;

import java.util.Map;

@Data
public class PipelineContext {

    /**
     * 输入的明细数据
     */
    private Map<String, Object> inputData;

    /**
     * 指标数据
     */
    private Map<String, Object> metricData;

}
