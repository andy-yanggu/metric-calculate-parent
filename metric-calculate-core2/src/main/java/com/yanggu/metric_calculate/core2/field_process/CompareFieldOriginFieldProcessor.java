package com.yanggu.metric_calculate.core2.field_process;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多字段比较和保留原始数据
 */
@Data
public class CompareFieldOriginFieldProcessor implements FieldProcessor<JSONObject, Object>  {

    private List<String> objectiveCompareFieldList;

    private Map<String, Class<?>> fieldMap;

    /**
     * 多字段排序字段处理器
     */
    private MultiFieldOrderFieldProcessor multiFieldOrderFieldProcessor;

    @Override
    public void init() throws Exception {
        List<FieldOrderParam> collect = objectiveCompareFieldList.stream()
                .map(tempCompareField -> new FieldOrderParam(tempCompareField, true))
                .collect(Collectors.toList());
        this.multiFieldOrderFieldProcessor = FieldProcessorUtil.getOrderFieldProcessor(fieldMap, collect);
    }

    @Override
    public Object process(JSONObject input) throws Exception {
        return null;
    }

}
