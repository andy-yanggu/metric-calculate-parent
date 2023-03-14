package com.yanggu.metric_calculate.core2.enums;

import cn.hutool.core.date.DateField;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时间单位
 */
@Getter
@AllArgsConstructor
public enum TimeUnit {

    /**
     * 毫秒
     */
    MILLS(DateField.MILLISECOND, 1L),

    /**
     * 秒
     */
    SECOND(DateField.SECOND, 1000L),

    /**
     * 分钟
     */
    MINUTE(DateField.MINUTE, 60L * SECOND.millis),

    /**
     * 小时
     */
    HOUR(DateField.HOUR_OF_DAY, 60L * MINUTE.millis),

    /**
     * 日
     */
    DAY(DateField.DAY_OF_YEAR, 24L * HOUR.millis),

    /**
     * 周
     */
    WEEK(DateField.WEEK_OF_YEAR, 7L * DAY.millis),

    /**
     * 月
     */
    MOUTH(DateField.MONTH, 30L * DAY.millis),

    /**
     * 季度
     */
    //SEASON(DateField.)

    /**
     * 年
     */
    YEAR(DateField.YEAR, 365L * DAY.millis);

    private final DateField dateField;

    private final Long millis;

    public Long toMillis(long length) {
        return this.millis * length;
    }

}
