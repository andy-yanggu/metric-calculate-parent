package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 只是定义了K的泛型为MultiFieldDistinctKey
 * <p>且定义了ValueOUT必须为可比较的</p>
 *
 * @param <V>        map的value类型
 * @param <ValueACC> value的累加器类型
 * @param <ValueOUT> value的输出类型
 * @param <OUT>      输出数据类型
 */
public abstract class AbstractMultiFieldDistinctKeyValueOutComparableMapAggregateFunction<V, ValueACC, ValueOUT extends Comparable<? super ValueOUT>, OUT>
        extends AbstractMultiFieldDistinctKeyMapAggregateFunction<V, ValueACC, ValueOUT, OUT> {

    /**
     * 根据value进行排序并进行截取
     *
     * @param accumulator
     * @param asc
     * @param limit
     * @return
     */
    protected Stream<AbstractMap.SimpleImmutableEntry<MultiFieldDistinctKey, ValueOUT>> getCompareLimitStream(
                                                                    Map<MultiFieldDistinctKey, ValueACC> accumulator,
                                                                    Boolean asc,
                                                                    Integer limit) {
        return accumulator.entrySet().stream()
                //映射成SimpleImmutableEntry
                .map(tempEntry -> new AbstractMap.SimpleImmutableEntry<>(tempEntry.getKey(), valueAggregateFunction.getResult(tempEntry.getValue())))
                //过滤掉value为null的值
                .filter(tempEntry -> tempEntry.getValue() != null)
                //根据ValueOUT进行排序
                .sorted((o1, o2) -> {
                    ValueOUT value1 = o1.getValue();
                    ValueOUT value2 = o2.getValue();
                    int compareTo = value1.compareTo(value2);
                    if (Boolean.TRUE.equals(asc)) {
                        return compareTo;
                    } else {
                        return -compareTo;
                    }
                })
                //进行截取
                .limit(limit);
    }

}
