package com.yanggu.metric_calculate.flink.pojo;

import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import lombok.Data;
import org.dromara.hutool.json.JSONObject;

/**
 * 派生指标计算数据
 */
@Data
public class DeriveCalculateData<IN, ACC, OUT> {

    /**
     * 派生指标的id
     */
    private Long deriveId;

    /**
     * 维度信息
     */
    private DimensionSet dimensionSet;

    /**
     * 明细数据
     */
    private JSONObject data;

    /**
     * 指标数据
     */
    private MetricCube<IN, ACC, OUT> metricCube;

}
