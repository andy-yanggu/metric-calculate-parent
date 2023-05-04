package com.yanggu.metric_calculate.core2.cube;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.table.Table;
import lombok.Data;

/**
 * 指标数据
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class MetricCube<IN, ACC, OUT> {

    /**
     * 指标的维度
     */
    private DimensionSet dimensionSet;

    /**
     * 指标数据
     */
    private Table<IN, ACC, OUT> table;

    /**
     * 查询指标数据
     *
     * @param input
     * @return
     */
    public DeriveMetricCalculateResult<OUT> query(JSONObject input) {
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setKey(dimensionSet.getKey());
        deriveMetricCalculateResult.setName(dimensionSet.getMetricName());
        deriveMetricCalculateResult.setDimensionMap(dimensionSet.getDimensionMap());
        table.query(input, deriveMetricCalculateResult);
        return deriveMetricCalculateResult;
    }

}
