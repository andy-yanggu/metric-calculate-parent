package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import org.dromara.hutool.core.lang.tuple.Pair;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
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
        AbstractMultiFieldSortedListAggregateFunction<JSONObject, JSONObject> {

    @Override
    public JSONObject inToOut(Pair<MultiFieldData, JSONObject> pair) {
        return pair.getRight();
    }

}
