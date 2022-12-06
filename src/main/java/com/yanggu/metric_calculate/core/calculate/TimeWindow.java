package com.yanggu.metric_calculate.core.calculate;

import lombok.Data;

/**
 * 包含start, 不包含end
 */
@Data
public class TimeWindow {

    private final long start;

    private final long end;

    public TimeWindow(long start, long end) {
        this.start = start;
        this.end = end;
    }

}
