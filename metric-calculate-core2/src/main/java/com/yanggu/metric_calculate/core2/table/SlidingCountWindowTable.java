package com.yanggu.metric_calculate.core2.table;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动计数窗口
 */
public class SlidingCountWindowTable<IN, ACC, OUT> implements Table<IN, ACC, OUT> {

    private List<ACC> accList = new ArrayList<>();

    @Override
    public void put(Long timestamp, IN in) {

    }

    @Override
    public OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return null;
    }
}
