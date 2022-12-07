package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.unit.MergedUnit;

/**
 * 对象型
 *
 * @param <T>
 * @param <U>
 */
public interface ObjectiveUnit<T, U extends ObjectiveUnit<T, U>> extends MergedUnit<U> {

    U value(T object);

}
