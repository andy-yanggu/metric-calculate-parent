package com.yanggu.metriccalculate.calculate;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.client.magiccube.pojo.Store;
import com.yanggu.metriccalculate.fieldprocess.DimensionSetProcessor;
import com.yanggu.metriccalculate.fieldprocess.FilterProcessor;
import com.yanggu.metriccalculate.fieldprocess.MetricFieldProcessor;
import com.yanggu.metriccalculate.fieldprocess.TimeFieldProcessor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 原子指标计算类
 */
@Data
@Slf4j
@NoArgsConstructor
public class AtomMetricCalculate implements Calculate<JSONObject, Object> {

    /**
     * 指标名称
     */
    private String name;

    /**
     * 前置过滤条件处理器, 进行过滤处理
     */
    private FilterProcessor filterProcessor;

    /**
     * 度量字段处理器, 提取出度量值
     */
    private MetricFieldProcessor<?> metricFieldProcessor;

    /**
     * 时间字段, 提取出时间戳
     */
    private TimeFieldProcessor timeFieldProcessor;

    /**
     * 维度字段处理器
     */
    private DimensionSetProcessor dimensionSetProcessor;

    /**
     * 存储宽表
     */
    private Store store;

    @Override
    public Object exec(JSONObject rtEvent) throws Exception {
        //执行前置过滤条件
        if (!filterProcessor.process(rtEvent)) {
            return null;
        }

        //执行度量表达式, 提取出度量字段的值
        Object process = metricFieldProcessor.process(rtEvent);
        if (process == null) {
            if (log.isDebugEnabled()) {
                log.debug("Get unit from input, but get null, input = {}", JSONUtil.toJsonStr(rtEvent));
            }
        }
        return process;
    }

}
