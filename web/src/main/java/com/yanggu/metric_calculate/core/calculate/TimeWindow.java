package com.yanggu.metric_calculate.core.calculate;

import lombok.Data;

/**
 * 包含start, 不包含end
 */
@Data
public class TimeWindow {

    /**
     * 窗口开始时间戳（包含）
     */
    private final long start;

    /**
     * 窗口结束时间戳（不包含）
     */
    private final long end;

    public TimeWindow(long start, long end) {
        this.start = start;
        this.end = end;
    }

}
