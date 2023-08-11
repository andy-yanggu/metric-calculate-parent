package com.yanggu.metric_calculate.flink.pojo;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import lombok.Data;

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
