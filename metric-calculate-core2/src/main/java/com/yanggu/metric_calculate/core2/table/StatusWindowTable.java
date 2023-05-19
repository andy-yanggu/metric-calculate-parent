package com.yanggu.metric_calculate.core2.table;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.metric_list.MetricListFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 状态窗口, 当窗口字段的值发生改变时, 生成一个新的窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class StatusWindowTable<IN, ACC, OUT> extends AbstractTable<IN, ACC, OUT> {

    private Map<String, Class<?>> fieldMap;

    private List<String> statusExpressList;

    private MetricListFieldProcessor metricListFieldProcessor;

    private List<Object> statusList;

    private List<IN> inList = new ArrayList<>();

    @Override
    public void init() {
        this.metricListFieldProcessor = FieldProcessorUtil.getMetricListFieldProcessor(fieldMap, statusExpressList);
    }

    @Override
    public void put(JSONObject input) {
        List<Object> newStatusList = metricListFieldProcessor.process(input);
        //如果状态不相同, 清空数据
        if (!newStatusList.equals(statusList)) {
            inList.clear();
        }
        this.statusList = newStatusList;
        //添加度量值
        inList.add(getInFromInput(input));
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query() {
        OUT outFromInList = aggregateFieldProcessor.getOutFromInList(inList);
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setStatusList(statusList);
        deriveMetricCalculateResult.setResult(outFromInList);
        return deriveMetricCalculateResult;
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query(JSONObject input) {
        List<Object> newStatusList = metricListFieldProcessor.process(input);
        //如果状态不相同, 清空数据
        if (!newStatusList.equals(statusList)) {
            inList.clear();
        }
        OUT outFromInList = aggregateFieldProcessor.getOutFromInList(inList);
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setStatusList(newStatusList);
        deriveMetricCalculateResult.setResult(outFromInList);
        return deriveMetricCalculateResult;
    }

    @Override
    public boolean isEmpty() {
        return CollUtil.isEmpty(inList);
    }

}
