package com.yanggu.metric_calculate.core.field_process.multi_field_order;

import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import lombok.Data;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.comparator.ComparatorChain;
import org.dromara.hutool.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多字段排序字段处理器
 */
@Data
public class MultiFieldOrderFieldProcessor implements FieldProcessor<JSONObject, MultiFieldOrderCompareKey> {

    private Map<String, Class<?>> fieldMap;

    private List<FieldOrderParam> fieldOrderParamList;

    private AviatorFunctionFactory aviatorFunctionFactory;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    private ComparatorChain<List<Object>> comparatorChain;

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
                .collect(Collectors.toList());

        List<Boolean> collect = fieldOrderParamList.stream()
                .map(FieldOrderParam::getIsAsc)
                .collect(Collectors.toList());

        //合并多个比较器
        this.comparatorChain = MultiFieldOrderCompareKey.getComparatorChain(collect);
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
        multiFieldOrderCompareKey.setComparatorChain(comparatorChain);
        return multiFieldOrderCompareKey;
    }

}
