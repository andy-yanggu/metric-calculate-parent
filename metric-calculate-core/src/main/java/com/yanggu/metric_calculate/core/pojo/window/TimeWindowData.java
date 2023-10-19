package com.yanggu.metric_calculate.core.pojo.window;

/**
 * 包含windowStart, 不包含windowEnd
 *
 * @param windowStart 窗口开始时间戳（包含）
 * @param windowEnd   窗口结束时间戳（不包含）
 */
public record TimeWindowData(long windowStart, long windowEnd) {
}
