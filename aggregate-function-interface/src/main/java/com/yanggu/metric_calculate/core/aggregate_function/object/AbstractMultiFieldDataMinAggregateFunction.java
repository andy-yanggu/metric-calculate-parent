package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.pojo.acc.ListObjectComparator;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.lang.tuple.Pair;

/**
 * 最小聚合函数抽象类
 * <p>子类需要重写{@link AggregateFunction#getResult(Object)}方法</p>
 *
 * @param <IN> 输入数据类型
 * @param <OUT> 输出数据类型
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractMultiFieldDataMinAggregateFunction<IN, OUT> extends AbstractMinAggregateFunction<Pair<MultiFieldData, IN>, OUT> {

    private Integer compareParamLength;

    @Override
    public void init() {
        ListObjectComparator<IN> listObjectComparator = ListObjectComparator.createInstance(compareParamLength);
        setComparator(listObjectComparator);
    }

}
