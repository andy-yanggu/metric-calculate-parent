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

    public String getRealKey() {
        return dimensionSet.getRealKey();
    }

    /**
     * 查询指标数据
     * <p>实时查询</p>
     *
     * @return
     */
    public DeriveMetricCalculateResult<OUT> query() {
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = table.query();
        if (deriveMetricCalculateResult == null) {
            return null;
        }
        //设置维度信息
        setDimension(deriveMetricCalculateResult);
        return deriveMetricCalculateResult;
    }

    /**
     * 查询指标数据
     * <p>主要用于查询历史数据</p>
     *
     * @param input
     * @return
     */
    public DeriveMetricCalculateResult<OUT> query(JSONObject input) {
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = table.query(input);
        if (deriveMetricCalculateResult == null) {
            return null;
        }
        //设置维度信息
        setDimension(deriveMetricCalculateResult);
        return deriveMetricCalculateResult;
    }

    /**
     * 设置维度信息
     *
     * @param deriveMetricCalculateResult
     */
    private void setDimension(DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult) {
        if (deriveMetricCalculateResult == null) {
            return;
        }
        deriveMetricCalculateResult.setKey(dimensionSet.getKey());
        deriveMetricCalculateResult.setName(dimensionSet.getMetricName());
        deriveMetricCalculateResult.setDimensionMap(dimensionSet.getDimensionMap());
    }

}
