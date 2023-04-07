package com.yanggu.metric_calculate.core2.pojo.metric;

import com.yanggu.metric_calculate.core2.enums.TimeUnitEnum;
import com.yanggu.metric_calculate.core2.util.DateUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TimeBaselineDimensionTest {

    private final long timestamp = DateUtils.parseDateTime("2023-03-30 14:02:23");

    /**
     * 测试毫秒
     */
    @Test
    public void getTimeWindow_MillisSecond() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.MILLS);
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(5L);
        assertEquals(2, timeWindowList.size());

        TimeWindow timeWindow = timeWindowList.get(0);
        assertEquals(4L, timeWindow.getWindowStart());
        assertEquals(6L, timeWindow.getWindowEnd());

        timeWindow = timeWindowList.get(1);
        assertEquals(5L, timeWindow.getWindowStart());
        assertEquals(7L, timeWindow.getWindowEnd());
    }

    /**
     * 测试秒
     */
    @Test
    public void getTimeWindow_Second() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.SECOND);
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowList.size());

        TimeWindow timeWindow = timeWindowList.get(0);
        assertEquals("2023-03-30 14:02:22", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-03-30 14:02:24", DateUtils.formatDateTime(timeWindow.getWindowEnd()));

        timeWindow = timeWindowList.get(1);
        assertEquals("2023-03-30 14:02:23", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-03-30 14:02:25", DateUtils.formatDateTime(timeWindow.getWindowEnd()));
    }

    /**
     * 测试分钟
     */
    @Test
    public void getTimeWindow_Minute() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.MINUTE);
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowList.size());

        TimeWindow timeWindow = timeWindowList.get(0);
        assertEquals("2023-03-30 14:01:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-03-30 14:03:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));

        timeWindow = timeWindowList.get(1);
        assertEquals("2023-03-30 14:02:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-03-30 14:04:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));
    }

    /**
     * 测试小时
     */
    @Test
    public void getTimeWindow_Hour() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.HOUR);
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowList.size());

        TimeWindow timeWindow = timeWindowList.get(0);
        assertEquals("2023-03-30 13:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-03-30 15:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));

        timeWindow = timeWindowList.get(1);
        assertEquals("2023-03-30 14:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-03-30 16:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));
    }

    /**
     * 测试天
     */
    @Test
    public void getTimeWindow_Day() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.DAY);
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowList.size());

        TimeWindow timeWindow = timeWindowList.get(0);
        assertEquals("2023-03-29 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-03-31 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));

        timeWindow = timeWindowList.get(1);
        assertEquals("2023-03-30 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-04-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));
    }

    /**
     * 测试周
     * <p>一周的开始是周一</p>
     */
    @Test
    public void getTimeWindow_Week() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.WEEK);
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowList.size());

        TimeWindow timeWindow = timeWindowList.get(0);
        assertEquals("2023-03-20 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-04-03 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));

        timeWindow = timeWindowList.get(1);
        assertEquals("2023-03-27 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-04-10 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));
    }

    /**
     * 测试月
     */
    @Test
    public void getTimeWindow_Month() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.MONTH);
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowList.size());

        TimeWindow timeWindow = timeWindowList.get(0);
        assertEquals("2023-02-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-04-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));

        timeWindow = timeWindowList.get(1);
        assertEquals("2023-03-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-05-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));
    }

    /**
     * 测试季度
     */
    @Test
    public void getTimeWindow_Quarter() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.QUARTER);
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowList.size());

        TimeWindow timeWindow = timeWindowList.get(0);
        assertEquals("2022-10-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-04-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));

        timeWindow = timeWindowList.get(1);
        assertEquals("2023-01-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2023-07-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));
    }

    /**
     * 测试年
     */
    @Test
    public void getTimeWindow_Year() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(2, TimeUnitEnum.YEAR);
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        assertEquals(2, timeWindowList.size());

        TimeWindow timeWindow = timeWindowList.get(0);
        assertEquals("2022-01-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2024-01-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));

        timeWindow = timeWindowList.get(1);
        assertEquals("2023-01-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowStart()));
        assertEquals("2025-01-01 00:00:00", DateUtils.formatDateTime(timeWindow.getWindowEnd()));
    }

}