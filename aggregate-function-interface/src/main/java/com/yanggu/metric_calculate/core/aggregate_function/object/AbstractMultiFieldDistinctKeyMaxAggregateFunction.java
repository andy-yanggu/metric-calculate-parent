package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.ListObjectComparator;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
        List<Boolean> booleanList = new ArrayList<>();
        for (int i = 0; i < compareParamLength; i++) {
            booleanList.add(Boolean.TRUE);
        }
        ListObjectComparator<IN> listObjectComparator = new ListObjectComparator<>();
        listObjectComparator.setBooleanList(booleanList);
        setComparator(listObjectComparator);
    }

}