package com.yanggu.metric_calculate.core2.util;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import static java.util.Calendar.*;

/**
 * 时间日期工具类
 */
public class DateUtils {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {
    }

    /**
     * 将时间戳格式化成yyyy-MM-dd HH:mm:ss字符串
     *
     * @param timestamp
     * @return
     */
    public static String formatDateTime(Long timestamp) {
        return format(timestamp, DATE_TIME_PATTERN);
    }

    /**
     * 时间戳格式化
     *
     * @param timestamp
     * @param pattern
     * @return
     */
    public static String format(Long timestamp, String pattern) {
        return DateTimeFormat.forPattern(pattern).print(timestamp);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的字符串解析成时间戳
     *
     * @param text
     * @return
     */
    public static long parseDateTime(String text) {
        return parse(text, DATE_TIME_PATTERN);
    }

    /**
     * 日期字符串解析
     *
     * @param text
     * @param pattern
     * @return
     */
    public static long parse(String text, String pattern) {
        return DateTimeFormat.forPattern(pattern).parseMillis(text);
    }

    /**
     * 修改为结束时间
     * <p>例如传入2023-03-28 18:39:22的时间戳和11(HOUR_OF_DAY)</p>
     * <p>返回2023-03-28 19:00:00的时间戳</p>
     *
     * @param timestamp
     * @param timeUnit
     * @return
     */
    public static long ceiling(long timestamp, int timeUnit) {
        return calculate(timestamp, timeUnit, true);
    }

    /**
     * 截取时间
     * <p>例如传入2023-03-28 18:39:22的时间戳和11(HOUR_OF_DAY)</p>
     * <p>返回2023-03-28 18:00:00的时间戳</p>
     *
     * @param timestamp
     * @param timeUnit
     * @return
     */
    public static long truncate(long timestamp, int timeUnit) {
        return calculate(timestamp, timeUnit, false);
    }

    /**
     * @param timestamp
     * @param timeUnit -1表示季度
     * @param flag      true ceiling false truncate
     * @return
     */
    private static long calculate(long timestamp, int timeUnit, boolean flag) {
        //毫秒值
        if (timeUnit == MILLISECOND) {
            return timestamp;
        }
        //季度
        //1-3第一季度、4-6第二季度、7-9第三季度、10-12第四季度
        DateTime dateTime = new DateTime(timestamp);
        if (timeUnit == -1) {
            int month = dateTime.getMonthOfYear();
            //第一季度
            if (month >= 1 && month <= 3) {
                if (flag) {
                    month = 4 - month;
                } else {
                    month = 1 - month;
                }
                //第二季度
            } else if (month >= 4 && month <= 6) {
                if (flag) {
                    month = 7 - month;
                } else {
                    month = 4 - month;
                }
                //第三季度
            } else if (month >= 7 && month <= 9) {
                if (flag) {
                    month = 10 - month;
                } else {
                    month = 7 - month;
                }
                //第四季度
            } else {
                if (flag) {
                    month = 13 - month;
                } else {
                    month = 10 - month;
                }
            }
            return dateTime.monthOfYear().roundFloorCopy().plusMonths(month).getMillis();
        }
        DateTime.Property property;
        //秒
        if (timeUnit == SECOND) {
            property = dateTime.secondOfMinute();
            //分钟
        } else if (timeUnit == MINUTE) {
            property = dateTime.minuteOfHour();
            //小时
        } else if (timeUnit == HOUR_OF_DAY) {
            property = dateTime.hourOfDay();
            //天
        } else if (timeUnit == DAY_OF_YEAR) {
            property = dateTime.dayOfYear();
            //周(一周的开始是星期一)
        } else if (timeUnit == WEEK_OF_YEAR) {
            property = dateTime.weekOfWeekyear();
            //月
        } else if (timeUnit == MONTH) {
            property = dateTime.monthOfYear();
            //年
        } else if (timeUnit == YEAR) {
            property = dateTime.year();
        } else {
            throw new RuntimeException("timeUnit: " + timeUnit + "类型错误");
        }
        if (flag) {
            return property.roundCeilingCopy().getMillis();
        } else {
            return property.roundFloorCopy().getMillis();
        }
    }

}
