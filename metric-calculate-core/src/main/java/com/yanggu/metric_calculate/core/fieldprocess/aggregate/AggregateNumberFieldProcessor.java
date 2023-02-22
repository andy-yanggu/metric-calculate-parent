package com.yanggu.metric_calculate.core.fieldprocess.aggregate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.StrUtil;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.fieldprocess.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 聚合数值类型处理器继承自度量字段处理器, 增加了聚合类型
 */
@Data
@Slf4j
@NoArgsConstructor
public class AggregateNumberFieldProcessor<T, M extends MergedUnit<M>> extends BaseAggregateFieldProcessor<T, M> {

    private MetricFieldProcessor<T, Object> metricFieldProcessor;

    private List<MetricFieldProcessor<T, Object>> metricFieldProcessorList;

    @Override
    public void init() throws Exception {
        super.init();
        Numerical numerical = mergeUnitClazz.getAnnotation(Numerical.class);
        if (numerical.multiNumber()) {
            List<String> metricExpressList = udafParam.getMetricExpressList();
            if (CollUtil.isEmpty(metricExpressList)) {
                throw new RuntimeException("度量字段列表为空");
            }
            this.metricFieldProcessorList = metricExpressList.stream()
                    .map(tempExpress -> FieldProcessorUtil.<T>getMetricFieldProcessor(fieldMap, tempExpress))
                    .collect(Collectors.toList());
        } else {
            String metricExpress = udafParam.getMetricExpress();
            if (StrUtil.isBlank(metricExpress)) {
                throw new RuntimeException("度量字段为空");
            }
            this.metricFieldProcessor =
                    FieldProcessorUtil.getMetricFieldProcessor(fieldMap, metricExpress);
        }
    }

    @Override
    @SneakyThrows
    public M process(T input) {
        Numerical numerical = mergeUnitClazz.getAnnotation(Numerical.class);
        Object process;
        if (numerical.multiNumber()) {
            List<Object> dataList = new ArrayList<>();
            for (MetricFieldProcessor<T, Object> fieldProcessor : this.metricFieldProcessorList) {
                Object tempData = fieldProcessor.process(input);
                if (tempData == null) {
                    return null;
                }
                dataList.add(UnitFactory.createCubeNumber(tempData));
            }
            process = new Tuple(dataList.toArray());
        } else {
            process = this.metricFieldProcessor.process(input);
        }
        if (process == null) {
            return null;
        }

        //生成MergedUnit
        return (M) unitFactory.initInstanceByValue(aggregateType, process, udafParam.getParam());
    }

}
