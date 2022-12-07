package com.yanggu.metric_calculate.core.unit;

public interface CollectionUnit<T, U extends CollectionUnit<T, U>> extends MergedUnit<U> {

    U add(T value);

}
