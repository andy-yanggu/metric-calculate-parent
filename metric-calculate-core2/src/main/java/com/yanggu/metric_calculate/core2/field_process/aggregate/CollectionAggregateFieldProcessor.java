package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.KeyValue;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Map;

@Setter
public class CollectionAggregateFieldProcessor<IN, ACC, OUT> extends AbstractAggregateFieldProcessor<IN, ACC, OUT> {

    private BaseUdafParam udafParam;

    private Map<String, Class<?>> fieldMap;

    /**
     * 对于滑动计数窗口和CEP类型, 需要额外的聚合处理器
     */
    private BaseUdafParam externalBaseUdafParam;

    /**
     * 多字段去重字段处理器
     */
    private MultiFieldDistinctFieldProcessor multiFieldDistinctFieldProcessor;

    /**
     * 多字段排序字段处理器
     */
    private MultiFieldOrderFieldProcessor multiFieldOrderFieldProcessor;

    /**
     * 保留字段字段处理器
     */
    private MetricFieldProcessor retainFieldValueFieldProcessor;

    private Collective collective;

    public CollectionAggregateFieldProcessor(AggregateFunction<IN, ACC, OUT> aggregateFunction) {
        super(aggregateFunction);
        this.collective = aggregateFunction.getClass().getAnnotation(Collective.class);
    }

    @Override
    public void init() throws Exception {
        //设置了去重字段
        if (collective.useDistinctField()) {
            this.multiFieldDistinctFieldProcessor =
                    FieldProcessorUtil.getDistinctFieldFieldProcessor(fieldMap, udafParam.getDistinctFieldList());
        }

        //设置了排序字段
        if (collective.useSortedField()) {
            this.multiFieldOrderFieldProcessor =
                    FieldProcessorUtil.getOrderFieldProcessor(fieldMap, udafParam.getCollectiveSortFieldList());
        }

        //设置了保留字段
        if (!collective.retainObject()) {
            this.retainFieldValueFieldProcessor =
                    FieldProcessorUtil.getMetricFieldProcessor(fieldMap, udafParam.getRetainExpress());
        }
    }

    @SneakyThrows
    @Override
    public IN process(JSONObject input) {

        //获取保留字段或者原始数据
        //CloneWrapper<Object> retainFieldValue = getRetainFieldValue(input, collective.retainObject());
        Object retainFieldValue = input;

        //默认没有去重字段或者排序字段
        Object result = input;

        //使用了去重字段
        if (collective.useDistinctField()) {
            MultiFieldDistinctKey distinctKey = multiFieldDistinctFieldProcessor.process(input);
            if (distinctKey == null) {
                return null;
            }
            result = new KeyValue<>(distinctKey, retainFieldValue);
        }

        //使用了排序字段
        if (collective.useSortedField()) {
            MultiFieldOrderCompareKey multiFieldOrderCompareKey = multiFieldOrderFieldProcessor.process(input);
            if (multiFieldOrderCompareKey == null) {
                return null;
            }
            result = new KeyValue<>(multiFieldOrderCompareKey, retainFieldValue);
        }
        return (IN) result;
    }

}
