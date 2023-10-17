package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.ListObjectComparator;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;
import lombok.Data;

/**
 * 最大值聚合函数抽象类
 * <p>子类需要重写{@link AggregateFunction#getResult(Object)}方法</p>
 *
 * @param <IN> 输入数据类型
 * @param <OUT> 输出数据类型
 */
@Data
public abstract class AbstractMultiFieldDistinctKeyMaxAggregateFunction<IN, OUT> extends AbstractMaxAggregateFunction<KeyValue<MultiFieldDistinctKey, IN>, OUT> {

    private Integer compareParamLength;

    @Override
    public void init() {
        ListObjectComparator<IN> listObjectComparator = ListObjectComparator.createInstance(compareParamLength);
        setComparator(listObjectComparator);
    }

}