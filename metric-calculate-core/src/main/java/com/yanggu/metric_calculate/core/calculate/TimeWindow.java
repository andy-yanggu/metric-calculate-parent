package com.yanggu.metric_calculate.core.calculate;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 包含start, 不包含end
 */
@Data
@AllArgsConstructor
public class TimeWindow {

    /**
     * 窗口开始时间戳（包含）
     */
    private final long windowStart;

    /**
     * 窗口结束时间戳（不包含）
     */
    private final long windowEnd;

}
