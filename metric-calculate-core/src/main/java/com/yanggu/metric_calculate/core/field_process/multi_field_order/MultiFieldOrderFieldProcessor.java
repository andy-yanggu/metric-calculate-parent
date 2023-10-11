package com.yanggu.metric_calculate.core.field_process.multi_field_order;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldOrderCompareKey;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 多字段排序字段处理器
 */
@Getter
@EqualsAndHashCode
public class MultiFieldOrderFieldProcessor implements FieldProcessor<JSONObject, MultiFieldOrderCompareKey> {

    private final Map<String, Class<?>> fieldMap;

    private final List<FieldOrderParam> fieldOrderParamList;

    private final AviatorFunctionFactory aviatorFunctionFactory;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    private List<Boolean> booleanList;

    public MultiFieldOrderFieldProcessor(Map<String, Class<?>> fieldMap,
                                         List<FieldOrderParam> fieldOrderParamList,
                                         AviatorFunctionFactory aviatorFunctionFactory) {
        this.fieldMap = fieldMap;
        this.fieldOrderParamList = fieldOrderParamList;
        this.aviatorFunctionFactory = aviatorFunctionFactory;
    }

    @Override
    public void init() throws Exception {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("明细宽表字段map为空");
        }

        if (CollUtil.isEmpty(fieldOrderParamList)) {
            throw new RuntimeException("排序字段为空");
        }

        if (aviatorFunctionFactory == null) {
            throw new RuntimeException("Aviator函数工厂类为空");
        }

        this.metricFieldProcessorList = fieldOrderParamList.stream()
                .map(tempFieldOrderParam -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, tempFieldOrderParam.getAviatorExpressParam(), aviatorFunctionFactory))
                .toList();

        this.booleanList = fieldOrderParamList.stream()
                .map(FieldOrderParam::getIsAsc)
                .toList();
    }

    @Override
    public MultiFieldOrderCompareKey process(JSONObject input) throws Exception {
        int size = fieldOrderParamList.size();
        List<Object> dataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            MetricFieldProcessor<Object> expression = metricFieldProcessorList.get(i);
            Object execute = expression.process(input);
            dataList.add(execute);
        }
        MultiFieldOrderCompareKey multiFieldOrderCompareKey = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey.setDataList(dataList);
        multiFieldOrderCompareKey.setBooleanList(booleanList);
        return multiFieldOrderCompareKey;
    }

}
