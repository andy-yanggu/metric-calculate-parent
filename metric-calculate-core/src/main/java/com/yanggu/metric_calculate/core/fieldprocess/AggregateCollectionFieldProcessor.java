package com.yanggu.metric_calculate.core.fieldprocess;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.KeyValue;
import lombok.Data;

@Data
public class AggregateCollectionFieldProcessor<M extends MergedUnit<M>> extends BaseAggregateFieldProcessor<M> {

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public M process(JSONObject input) throws Exception {
        //获取度量值
        Object execute = super.process(input);
        if (execute == null) {
            return null;
        }

        KeyValue keyValue = new KeyValue((Comparable) execute, input);
        return (M) unitFactory.initInstanceByValue(aggregateType, keyValue, udafParams);
    }

}
