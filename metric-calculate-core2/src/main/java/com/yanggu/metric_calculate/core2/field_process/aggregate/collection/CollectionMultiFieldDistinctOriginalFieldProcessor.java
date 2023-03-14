package com.yanggu.metric_calculate.core2.field_process.aggregate.collection;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.KeyValue;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;

import java.util.Map;

/**
 * 集合型, 多字段去重和保留原始数据
 *
 * @param <T>
 */
@Data
public class CollectionMultiFieldDistinctOriginalFieldProcessor
        implements FieldProcessor<JSONObject, KeyValue<MultiFieldDistinctKey, JSONObject>> {

    private BaseUdafParam baseUdafParam;

    private Map<String, Class<?>> fieldMap;

    private MultiFieldDistinctFieldProcessor multiFieldDistinctFieldProcessor;

    @Override
    public void init() throws Exception {
        this.multiFieldDistinctFieldProcessor = FieldProcessorUtil
                .getDistinctFieldFieldProcessor(fieldMap, baseUdafParam.getDistinctFieldList());
    }

    @Override
    public KeyValue<MultiFieldDistinctKey, JSONObject> process(JSONObject input) throws Exception {
        MultiFieldDistinctKey multiFieldDistinctKey = multiFieldDistinctFieldProcessor.process(input);
        return new KeyValue<>(multiFieldDistinctKey, input);
    }

}
