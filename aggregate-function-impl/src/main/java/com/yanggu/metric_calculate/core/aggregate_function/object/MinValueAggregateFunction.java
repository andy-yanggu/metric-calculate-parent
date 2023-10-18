package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.lang.tuple.Pair;

import java.util.List;

/**
 * 最小值
 */
@Objective(keyStrategy = 3, retainStrategy = 0)
@AggregateFunctionAnnotation(name = "MINVALUE", displayName = "最小值")
public class MinValueAggregateFunction extends AbstractMultiFieldDataMinAggregateFunction<Void, List<Object>> {

    @Override
    public List<Object> getResult(MutableObj<Pair<MultiFieldData, Void>> accumulator) {
        return accumulator.get().getLeft().getFieldList();
    }

}
