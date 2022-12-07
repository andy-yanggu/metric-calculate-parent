package com.yanggu.metric_calculate.core.unit.collection;

import com.yanggu.metric_calculate.core.unit.MergedUnit;

/**
 * 集合型
 * @param <T>
 * @param <U>
 */
public interface CollectionUnit<T, U extends CollectionUnit<T, U>> extends MergedUnit<U> {

    U add(T value);

}
