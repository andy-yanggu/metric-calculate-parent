package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.json.JSONObject;

/**
 * 最小对象
 */
@Objective(keyStrategy = 3, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "MINOBJECT", displayName = "最小对象")
public class MinObjectAggregateFunction extends AbstractMultiFieldDistinctKeyMinAggregateFunction<JSONObject, JSONObject> {

    @Override
    public JSONObject getResult(MutableObj<KeyValue<MultiFieldDistinctKey, JSONObject>> accumulator) {
        return accumulator.get().getValue();
    }

}
