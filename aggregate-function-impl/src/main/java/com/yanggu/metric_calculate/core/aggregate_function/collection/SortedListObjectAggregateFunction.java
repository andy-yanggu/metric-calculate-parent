package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldOrderCompareKey;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.json.JSONObject;

/**
 * 有序对象列表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Collective(keyStrategy = 2, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "SORTEDLIMITLISTOBJECT", displayName = "有序对象列表")
public class SortedListObjectAggregateFunction extends
        AbstractSortedListAggregateFunction<KeyValue<MultiFieldOrderCompareKey, JSONObject>, JSONObject> {

    @Override
    public JSONObject inToOut(KeyValue<MultiFieldOrderCompareKey, JSONObject> input) {
        return input.getValue();
    }

}
