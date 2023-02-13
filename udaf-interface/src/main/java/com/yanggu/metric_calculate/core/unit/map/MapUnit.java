package com.yanggu.metric_calculate.core.unit.map;

import com.yanggu.metric_calculate.core.unit.MergedUnit;


public interface MapUnit<K, V, U extends MapUnit<K, V, U>> extends MergedUnit<U> {

    U put(K key, V value);

}
