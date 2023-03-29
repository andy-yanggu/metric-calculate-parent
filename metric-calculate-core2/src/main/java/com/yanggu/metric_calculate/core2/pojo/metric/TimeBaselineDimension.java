package com.yanggu.metric_calculate.core2.pojo.metric;

import com.yanggu.metric_calculate.core2.enums.TimeUnit;
import com.yanggu.metric_calculate.core2.util.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间聚合粒度
 * 时间单位和时间长度
 */
@Data
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class TimeBaselineDimension {

    /**
     * 时间长度
     */
    private Integer length;

    /**
     * 时间单位
     */
    private TimeUnit unit;

    public TimeBaselineDimension(Integer length, TimeUnit unit) {
        this.length = length;
        this.unit = unit;
    }

    /**
     * 当前数据聚合的时间戳
     * 例如数据时间为2022-11-21 14:00:00, 时间单位为DAY, 返回2022-11-21 00:00:00的时间戳
     *
     * @return
     */
    public Long getCurrentAggregateTimestamp(Long timestamp) {
        return DateUtils.truncate(timestamp, unit.getDateField());
    }

    /**
     * 包含左区间, 不包含右区间
     * <p>例如数据时间为2022-11-21 14:00:00, 时间单位为DAY, 时间长度为2, 也就是过去2天</p>
     * <p>时间区间为[2022-11-20 00:00:00, 2022-11-22 00:00:00), 左闭右开</p>
     * <p>时间区间为[2022-11-21 00:00:00, 2022-11-23 00:00:00), 左闭右开</p>
     * <p>如果时间聚合粒度不是1, 就是滑动窗口, 窗口滑动步长就是1个时间单位</p>
     */
    public List<TimeWindow> getTimeWindow2(Long timestamp) {
        int timeUnit = unit.getDateField();
        long tempTimestamp = timestamp;
        List<TimeWindow> windows = new ArrayList<>();
        //如果是季度
        //if (timeUnit == -1) {
        //
        //}
        //for (int i = length; i >= 0; i--) {
        //    long tempWindowStat = 0L;
        //    long tempWindowEnd = 0L;
        //    //if (timeUnit == MILLISECOND) {
        //    //    tempWindowStat = timestamp;
        //    //    tempWindowEnd = timestamp + length;
        //    //}
        //    ////季度
        //    ////1-3第一季度、4-6第二季度、7-9第三季度、10-12第四季度
        //    DateTime dateTime = new DateTime(tempTimestamp);
        //    if (timeUnit == -1) {
        //        int month = dateTime.getMonthOfYear();
        //        //第一季度
        //        if (month >= 1 && month <= 3) {
        //            if (flag) {
        //                month = 4 - month;
        //            } else {
        //                month = 1 - month;
        //            }
        //            //第二季度
        //        } else if (month >= 4 && month <= 6) {
        //            if (flag) {
        //                month = 7 - month;
        //            } else {
        //                month = 4 - month;
        //            }
        //            //第三季度
        //        } else if (month >= 7 && month <= 9) {
        //            if (flag) {
        //                month = 10 - month;
        //            } else {
        //                month = 7 - month;
        //            }
        //            //第四季度
        //        } else {
        //            if (flag) {
        //                month = 13 - month;
        //            } else {
        //                month = 10 - month;
        //            }
        //        }
        //        dateTime.monthOfYear().roundFloorCopy().plusMonths(month).getMillis();
        //    }
        //    //DateTime.Property property;
        //    ////秒
        //    //if (timeUnit == SECOND) {
        //    //    property = dateTime.secondOfMinute();
        //    //    //分钟
        //    //} else if (timeUnit == MINUTE) {
        //    //    property = dateTime.minuteOfHour();
        //    //    //小时
        //    //} else if (timeUnit == HOUR_OF_DAY) {
        //    //    property = dateTime.hourOfDay();
        //    //    //天
        //    //} else if (timeUnit == DAY_OF_YEAR) {
        //    //    property = dateTime.dayOfYear();
        //    //    //周(一周的开始是星期一)
        //    //} else if (timeUnit == WEEK_OF_YEAR) {
        //    //    property = dateTime.weekOfWeekyear();
        //    //    //月
        //    //} else if (timeUnit == MONTH) {
        //    //    property = dateTime.monthOfYear();
        //    //    //年
        //    //} else if (timeUnit == YEAR) {
        //    //    property = dateTime.year();
        //    //} else {
        //    //    throw new RuntimeException("timeUnit: " + timeUnit + "类型错误");
        //    //}
        //    //if (flag) {
        //    //    return property.roundCeilingCopy().getMillis();
        //    //} else {
        //    //    return property.roundFloorCopy().getMillis();
        //    //}
        //    TimeWindow timeWindow = new TimeWindow(tempWindowStat, tempWindowEnd);
        //    windows.add(timeWindow);
        //}

        return windows;
    }

    //TODO 需要修复getTimeWindow的bug
    public List<TimeWindow> getTimeWindow(Long timestamp) {
        Long windowEnd = DateUtils.ceiling(timestamp, this.unit.getDateField());
        long windowSize = java.util.concurrent.TimeUnit.DAYS.toMillis(1L) * length;
        long slidingSize = java.util.concurrent.TimeUnit.DAYS.toMillis(1L);
        long windowStart = windowEnd - windowSize;
        List<TimeWindow> windows = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            windows.add(new TimeWindow(windowStart + i * slidingSize, windowStart + i * slidingSize + windowSize));
        }
        return windows;
    }

}
