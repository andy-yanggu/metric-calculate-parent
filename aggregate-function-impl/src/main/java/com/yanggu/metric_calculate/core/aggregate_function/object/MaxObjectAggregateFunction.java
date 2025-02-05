package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.lang.tuple.Pair;

import java.util.Map;

/**
 * 最大对象
 */
@Objective(keyStrategy = 3, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "MAXOBJECT", displayName = "最大对象")
public class MaxObjectAggregateFunction extends AbstractMultiFieldDataMaxAggregateFunction<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> getResult(MutableObj<Pair<MultiFieldData, Map<String, Object>>> accumulator) {
        return accumulator.get().getRight();
    }

}
