package com.yanggu.metric_calculate.core.pojo.window;

import com.yanggu.metric_calculate.core.enums.TimeUnitEnum;
import com.yanggu.metric_calculate.core.util.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static java.util.Calendar.*;

/**
 * 时间聚合粒度
 * 时间单位和时间长度
 */
@Data
@Slf4j
@NoArgsConstructor
public class TimeBaselineDimension {

    /**
     * 时间长度
     */
    private Integer length;

    /**
     * 时间单位
     */
    private TimeUnitEnum unit;

    public TimeBaselineDimension(Integer length, TimeUnitEnum unit) {
        this.length = length;
        this.unit = unit;
    }

    /**
     * 返回当前数据聚合的时间戳
     * <p>例如数据时间为2022-11-21 14:00:00, 时间单位为DAY, 返回2022-11-21 00:00:00的时间戳</p>
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
    public List<TimeWindowData> getTimeWindowList(Long timestamp) {
        int timeUnit = unit.getDateField();
        List<TimeWindowData> windows = new ArrayList<>();
        //毫秒
        if (timeUnit == MILLISECOND) {
            for (int i = length - 1; i >= 0; i--) {
                windows.add(new TimeWindowData(timestamp - i, timestamp - i + length));
            }
            //秒
        } else if (timeUnit == SECOND) {
            DateTime dateTime = new DateTime(timestamp).secondOfMinute().roundFloorCopy();
            for (int i = length - 1; i >= 0; i--) {
                long windowStart = dateTime.minusSeconds(i).getMillis();
                long windowEnd = dateTime.plusSeconds(length - i).getMillis();
                windows.add(new TimeWindowData(windowStart, windowEnd));
            }
            //分钟
        } else if (timeUnit == MINUTE) {
            DateTime dateTime = new DateTime(timestamp).minuteOfHour().roundFloorCopy();
            for (int i = length - 1; i >= 0; i--) {
                long windowStart = dateTime.minusMinutes(i).getMillis();
                long windowEnd = dateTime.plusMinutes(length - i).getMillis();
                windows.add(new TimeWindowData(windowStart, windowEnd));
            }
            //小时
        } else if (timeUnit == HOUR_OF_DAY) {
            DateTime dateTime = new DateTime(timestamp).hourOfDay().roundFloorCopy();
            for (int i = length - 1; i >= 0; i--) {
                long windowStart = dateTime.minusHours(i).getMillis();
                long windowEnd = dateTime.plusHours(length - i).getMillis();
                windows.add(new TimeWindowData(windowStart, windowEnd));
            }
            //日
        } else if (timeUnit == DAY_OF_YEAR) {
            DateTime dateTime = new DateTime(timestamp).dayOfYear().roundFloorCopy();
            for (int i = length - 1; i >= 0; i--) {
                long windowStart = dateTime.minusDays(i).getMillis();
                long windowEnd = dateTime.plusDays(length - i).getMillis();
                windows.add(new TimeWindowData(windowStart, windowEnd));
            }
            //周
        } else if (timeUnit == WEEK_OF_YEAR) {
            DateTime dateTime = new DateTime(timestamp).weekOfWeekyear().roundFloorCopy();
            for (int i = length - 1; i >= 0; i--) {
                long windowStart = dateTime.minusWeeks(i).getMillis();
                long windowEnd = dateTime.plusWeeks(length - i).getMillis();
                windows.add(new TimeWindowData(windowStart, windowEnd));
            }
            //月
        } else if (timeUnit == MONTH) {
            DateTime dateTime = new DateTime(timestamp).monthOfYear().roundFloorCopy();
            for (int i = length - 1; i >= 0; i--) {
                long windowStart = dateTime.minusMonths(i).getMillis();
                long windowEnd = dateTime.plusMonths(length - i).getMillis();
                windows.add(new TimeWindowData(windowStart, windowEnd));
            }
            //季度
        } else if (timeUnit == -1) {
            DateTime dateTime = new DateTime(timestamp);
            int month = dateTime.getMonthOfYear();
            dateTime = new DateTime(timestamp).monthOfYear().roundFloorCopy().withMonthOfYear((month + 2) / 3);
            for (int i = length - 1; i >= 0; i--) {
                long windowStart = dateTime.minusMonths(i * 3).getMillis();
                long windowEnd = dateTime.plusMonths((length - i) * 3).getMillis();
                windows.add(new TimeWindowData(windowStart, windowEnd));
            }
            //年
        } else if (timeUnit == YEAR) {
            DateTime dateTime = new DateTime(timestamp).year().roundFloorCopy();
            for (int i = length - 1; i >= 0; i--) {
                long windowStart = dateTime.minusYears(i).getMillis();
                long windowEnd = dateTime.plusYears(length - i).getMillis();
                windows.add(new TimeWindowData(windowStart, windowEnd));
            }
        }
        return windows;
    }

    /**
     * 获取过期的时间戳
     * <p>默认是2倍长度的时间周期</p>
     *
     * @param timestamp
     * @return
     */
    public Long getExpireTimestamp(Long timestamp) {
        int timeUnit = unit.getDateField();
        int expireLength = 2 * length;
        //毫秒
        if (timeUnit == MILLISECOND) {
            return timestamp - expireLength;
            //秒
        } else if (timeUnit == SECOND) {
            return new DateTime(timestamp)
                    .secondOfMinute()
                    .roundFloorCopy()
                    .plusSeconds(expireLength)
                    .getMillis();
            //分钟
        } else if (timeUnit == MINUTE) {
            return new DateTime(timestamp)
                    .minuteOfHour()
                    .roundFloorCopy()
                    .minusMinutes(expireLength)
                    .getMillis();
            //小时
        } else if (timeUnit == HOUR_OF_DAY) {
            return new DateTime(timestamp)
                    .hourOfDay()
                    .roundFloorCopy()
                    .minusHours(expireLength)
                    .getMillis();
            //日
        } else if (timeUnit == DAY_OF_YEAR) {
            return new DateTime(timestamp)
                    .dayOfYear()
                    .roundFloorCopy()
                    .minusDays(expireLength)
                    .getMillis();
            //周
        } else if (timeUnit == WEEK_OF_YEAR) {
            return new DateTime(timestamp)
                    .weekOfWeekyear()
                    .roundFloorCopy()
                    .minusWeeks(expireLength)
                    .getMillis();
            //月
        } else if (timeUnit == MONTH) {
            return new DateTime(timestamp)
                    .monthOfYear()
                    .roundFloorCopy()
                    .minusMonths(expireLength)
                    .getMillis();
            //季度
        } else if (timeUnit == -1) {
            int month = new DateTime(timestamp).getMonthOfYear();
            return new DateTime(timestamp)
                    .monthOfYear()
                    .roundFloorCopy()
                    .withMonthOfYear((month + 2) / 3)
                    .minusMonths(expireLength)
                    .getMillis();
            //年
        } else if (timeUnit == YEAR) {
            return new DateTime(timestamp)
                    .year()
                    .roundFloorCopy()
                    .minusYears(expireLength)
                    .getMillis();
        } else {
            throw new RuntimeException("时间单位错误: " + unit);
        }
    }

}
