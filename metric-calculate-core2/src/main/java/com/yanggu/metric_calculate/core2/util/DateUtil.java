package com.yanggu.metric_calculate.core2.util;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateUtil {

    private DateUtil() {
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
        DateTime dateTime = new DateTime(timestamp);
        return 0L;
    }

    /**
     * 修改为起始时间
     * <p>例如传入2023-03-28 18:39:22的时间戳和11(HOUR_OF_DAY)</p>
     * <p>返回2023-03-28 18:00:00的时间戳</p>
     *
     * @param timestamp
     * @param timeUnit
     * @return
     */
    public static long truncate(long timestamp, int timeUnit) {
        DateTime dateTime = new DateTime(timestamp);
        return 0L;
    }

}
