package com.yanggu.metric_calculate.core.fieldprocess.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 聚合数值类型处理器继承自度量字段处理器, 增加了聚合类型
 */
@Data
@Slf4j
@NoArgsConstructor
public class AggregateNumberFieldProcessor<T, M extends MergedUnit<M>> extends BaseAggregateFieldProcessor<T, M> {

    private MetricFieldProcessor<T, Object> metricFieldProcessor;

    @Override
    public void init() throws Exception {
        super.init();
        this.metricFieldProcessor =
                FieldProcessorUtil.getMetricFieldProcessor(getFieldMap(), udafParam.getMetricExpress());
    }

    @Override
    @SneakyThrows
    public M process(T input) {
        Object process = metricFieldProcessor.process(input);
        if (process == null) {
            return null;
        }

        //生成MergedUnit
        return (M) unitFactory.initInstanceByValue(aggregateType, process, udafParam.getParam());
    }

}
