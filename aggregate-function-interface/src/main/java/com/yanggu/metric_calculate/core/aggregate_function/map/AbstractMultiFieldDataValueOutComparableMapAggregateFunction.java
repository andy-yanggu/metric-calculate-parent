package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.core.lang.tuple.Pair;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 定义了K的泛型为MultiFieldData
 * <p>且定义了ValueOUT必须为可比较的</p>
 *
 * @param <V>        map的value类型
 * @param <ValueACC> value的累加器类型
 * @param <ValueOUT> value的输出类型
 * @param <OUT>      输出数据类型
 */
public abstract class AbstractMultiFieldDataValueOutComparableMapAggregateFunction<V, ValueACC, ValueOUT extends Comparable<? super ValueOUT>, OUT>
        extends AbstractMultiFieldDataMapAggregateFunction<V, ValueACC, ValueOUT, OUT> {

    /**
     * 根据value进行排序并进行截取
     *
     * @param accumulator
     * @param asc
     * @param limit
     * @return
     */
    protected Stream<Pair<MultiFieldData, ValueOUT>> getCompareLimitStream(
                                                                    Map<MultiFieldData, ValueACC> accumulator,
                                                                    Boolean asc,
                                                                    Integer limit) {
        Comparator<ValueOUT> comparator;
        if (asc.equals(Boolean.TRUE)) {
            comparator = Comparator.nullsFirst(Comparator.naturalOrder());
        } else {
            comparator = Comparator.nullsLast(Comparator.<ValueOUT>naturalOrder().reversed());
        }
        return accumulator.entrySet().stream()
                //映射成Pair<MultiFieldData, ValueOUT>
                .map(tempEntry -> new Pair<>(tempEntry.getKey(), valueAggregateFunction.getResult(tempEntry.getValue())))
                //根据ValueOUT进行排序
                .sorted((o1, o2) -> comparator.compare(o1.getRight(), o2.getRight()))
                //进行截取
                .limit(limit);
    }

}
