package com.yanggu.metriccalculate.cube;



import com.yanggu.metriccalculate.fieldprocess.TimeBaselineDimension;
import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

/**
 * 时间序列存储
 *
 *
 * @param <V>
 */
@Data
public class TimeSeriesKVTable<V> extends TreeMap<Long, V> {

    private TimeBaselineDimension timeBaselineDimension;

    public void putValue(Long key, V value) {
        Long currentAggregateTimestamp = timeBaselineDimension.getCurrentAggregateTimestamp(key);
        //compute(key, (k, v) -> v == null ? value : )
    }

    //public V putValue(Long key, V value) {
    //    //return
    //}

}
