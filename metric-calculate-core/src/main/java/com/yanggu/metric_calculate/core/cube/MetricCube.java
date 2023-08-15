package com.yanggu.metric_calculate.core.cube;

import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.window.AbstractWindow;
import lombok.Data;
import org.dromara.hutool.json.JSONObject;

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
    private AbstractWindow<IN, ACC, OUT> window;

    public String getRealKey() {
        return dimensionSet.getRealKey();
    }

    /**
     * 添加明细数据到窗口中
     *
     * @param input
     */
    public void put(JSONObject input) {
        if (window == null) {
            return;
        }
        window.put(input);
    }

    /**
     * 查询指标数据
     * <p>无状态查询操作</p>
     * <p>实时查询</p>
     *
     * @return
     */
    public DeriveMetricCalculateResult<OUT> query() {
        if (isEmpty()) {
            return null;
        }
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = window.query();
        if (deriveMetricCalculateResult == null) {
            return null;
        }
        //设置维度信息
        setDimension(deriveMetricCalculateResult);
        return deriveMetricCalculateResult;
    }

    /**
     * 根据明细数据查询实时指标数据
     * <p>无状态查询操作</p>
     * <p>主要重新重新设定窗口条件。例如窗口内的数据是否过期</p>
     *
     * @param input
     * @return
     */
    public DeriveMetricCalculateResult<OUT> query(JSONObject input) {
        if (isEmpty()) {
            return null;
        }
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = window.query(input);
        if (deriveMetricCalculateResult == null) {
            return null;
        }
        //设置维度信息
        setDimension(deriveMetricCalculateResult);
        return deriveMetricCalculateResult;
    }

    public void deleteData() {
        if (isEmpty()) {
            return;
        }
        window.deleteData();
    }

    /**
     * 判断是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return window == null || window.isEmpty();
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
