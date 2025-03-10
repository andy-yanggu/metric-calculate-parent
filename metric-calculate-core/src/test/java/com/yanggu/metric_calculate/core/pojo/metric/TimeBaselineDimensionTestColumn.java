package com.yanggu.metric_calculate.core.pojo.metric;

import com.yanggu.metric_calculate.core.enums.TimeUnitEnum;
import com.yanggu.metric_calculate.core.pojo.window.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.pojo.window.TimeWindowData;
import com.yanggu.metric_calculate.core.util.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeBaselineDimensionTestColumn {

    private final long timestamp = DateUtils.parseDateTime("2023-03-30 14:02:23");

    @Test
    void testGetCurrentAggregateTimestamp() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.HOUR);
        Long currentAggregateTimestamp = timeBaselineDimension.getCurrentAggregateTimestamp(timestamp);
        assertEquals("2023-03-30 14:00:00", DateUtils.formatDateTime(currentAggregateTimestamp));
    }

    /**
     * 测试毫秒
     */
    @Test
    void getTimeWindow_MillisSecond() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.MILLS);
        List<TimeWindowData> timeWindowDataList = timeBaselineDimension.getTimeWindowList(5L);
        assertEquals(2, timeWindowDataList.size());

        TimeWindowData timeWindowData = timeWindowDataList.getFirst();
        assertEquals(4L, timeWindowData.windowStart());
        assertEquals(6L, timeWindowData.windowEnd());

        timeWindowData = timeWindowDataList.get(1);
        assertEquals(5L, timeWindowData.windowStart());
        assertEquals(7L, timeWindowData.windowEnd());
    }

    /**
     * 测试秒
     */
    @Test
    void getTimeWindow_Second() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.SECOND);
        List<TimeWindowData> timeWindowDataList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowDataList.size());

        TimeWindowData timeWindowData = timeWindowDataList.getFirst();
        assertEquals("2023-03-30 14:02:22", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-03-30 14:02:24", DateUtils.formatDateTime(timeWindowData.windowEnd()));

        timeWindowData = timeWindowDataList.get(1);
        assertEquals("2023-03-30 14:02:23", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-03-30 14:02:25", DateUtils.formatDateTime(timeWindowData.windowEnd()));
    }

    /**
     * 测试分钟
     */
    @Test
    void getTimeWindow_Minute() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.MINUTE);
        List<TimeWindowData> timeWindowDataList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowDataList.size());

        TimeWindowData timeWindowData = timeWindowDataList.getFirst();
        assertEquals("2023-03-30 14:01:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-03-30 14:03:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));

        timeWindowData = timeWindowDataList.get(1);
        assertEquals("2023-03-30 14:02:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-03-30 14:04:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));
    }

    /**
     * 测试小时
     */
    @Test
    void getTimeWindow_Hour() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.HOUR);
        List<TimeWindowData> timeWindowDataList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowDataList.size());

        TimeWindowData timeWindowData = timeWindowDataList.getFirst();
        assertEquals("2023-03-30 13:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-03-30 15:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));

        timeWindowData = timeWindowDataList.get(1);
        assertEquals("2023-03-30 14:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-03-30 16:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));
    }

    /**
     * 测试天
     */
    @Test
    void getTimeWindow_Day() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.DAY);
        List<TimeWindowData> timeWindowDataList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowDataList.size());

        TimeWindowData timeWindowData = timeWindowDataList.getFirst();
        assertEquals("2023-03-29 00:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-03-31 00:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));

        timeWindowData = timeWindowDataList.get(1);
        assertEquals("2023-03-30 00:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-04-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));
    }

    /**
     * 测试周
     * <p>一周的开始是周一</p>
     */
    @Test
    void getTimeWindow_Week() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.WEEK);
        List<TimeWindowData> timeWindowDataList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowDataList.size());

        TimeWindowData timeWindowData = timeWindowDataList.getFirst();
        assertEquals("2023-03-20 00:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-04-03 00:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));

        timeWindowData = timeWindowDataList.get(1);
        assertEquals("2023-03-27 00:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-04-10 00:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));
    }

    /**
     * 测试月
     */
    @Test
    void getTimeWindow_Month() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.MONTH);
        List<TimeWindowData> timeWindowDataList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowDataList.size());

        TimeWindowData timeWindowData = timeWindowDataList.getFirst();
        assertEquals("2023-02-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-04-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));

        timeWindowData = timeWindowDataList.get(1);
        assertEquals("2023-03-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-05-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));
    }

    /**
     * 测试季度
     */
    @Test
    void getTimeWindow_Quarter() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.QUARTER);
        List<TimeWindowData> timeWindowDataList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowDataList.size());

        TimeWindowData timeWindowData = timeWindowDataList.getFirst();
        assertEquals("2022-10-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-04-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));

        timeWindowData = timeWindowDataList.get(1);
        assertEquals("2023-01-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2023-07-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));
    }

    /**
     * 测试年
     */
    @Test
    void getTimeWindow_Year() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.YEAR);
        List<TimeWindowData> timeWindowDataList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowDataList.size());

        TimeWindowData timeWindowData = timeWindowDataList.getFirst();
        assertEquals("2022-01-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2024-01-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));

        timeWindowData = timeWindowDataList.get(1);
        assertEquals("2023-01-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowStart()));
        assertEquals("2025-01-01 00:00:00", DateUtils.formatDateTime(timeWindowData.windowEnd()));
    }

    /**
     * 测试小时
     */
    @Test
    void testGetExpireTimestamp_hour() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.HOUR);
        Long expireTimestamp = timeBaselineDimension.getExpireTimestamp(timestamp);
        assertEquals("2023-03-30 10:00:00", DateUtils.formatDateTime(expireTimestamp));
    }

    /**
     * 测试天
     */
    @Test
    void testGetExpireTimestamp_day() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.DAY);
        Long expireTimestamp = timeBaselineDimension.getExpireTimestamp(timestamp);
        assertEquals("2023-03-26 00:00:00", DateUtils.formatDateTime(expireTimestamp));
    }

}