package com.yanggu.metric_calculate.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.WEEK_OF_YEAR;

/**
 * 时间单位
 */
@Getter
@AllArgsConstructor
public enum TimeUnitEnum {

    /**
     * 毫秒
     */
    MILLS(MILLISECOND),

    /**
     * 秒
     */
    SECOND(java.util.Calendar.SECOND),

    /**
     * 分钟
     */
    MINUTE(java.util.Calendar.MINUTE),

    /**
     * 小时(24小时)
     */
    HOUR(HOUR_OF_DAY),

    /**
     * 日
     */
    DAY(DAY_OF_YEAR),

    /**
     * 周(周一是星期一)
     */
    WEEK(WEEK_OF_YEAR),

    /**
     * 月
     */
    MONTH(java.util.Calendar.MONTH),

    /**
     * 季度
     */
    QUARTER(-1),

    /**
     * 年
     */
    YEAR(java.util.Calendar.YEAR);

    private final int dateField;

}
