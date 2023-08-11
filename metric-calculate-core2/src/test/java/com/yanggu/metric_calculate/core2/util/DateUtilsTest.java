package com.yanggu.metric_calculate.core2.util;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static java.util.Calendar.*;
import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    /**
     * test format
     */
    @Test
    void test1() {
        long timestamp = 1679988328000L;
        assertEquals("2023-03-28 15:25:28", DateUtils.formatDateTime(timestamp));
    }

    /**
     * test parse
     */
    @Test
    void test2() {
        long time = DateUtils.parseDateTime("2023-03-28 15:25:28");
        assertEquals(1679988328000L, time);
    }

    /**
     * 测试毫秒、秒、分钟、小时、天、周、月、年
     */
    @Test
    void test3() {
        long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        //毫秒
        assertEquals(DateUtil.ceiling(date, DateField.MILLISECOND).getTime(), DateUtils.ceiling(timestamp, MILLISECOND));
        assertEquals(DateUtil.truncate(date, DateField.MILLISECOND).getTime(), DateUtils.truncate(timestamp, MILLISECOND));

        //秒
        assertEquals(DateUtil.ceiling(date, DateField.SECOND).getTime() + 1, DateUtils.ceiling(timestamp, SECOND));
        assertEquals(DateUtil.truncate(date, DateField.SECOND).getTime(), DateUtils.truncate(timestamp, SECOND));

        //分钟
        assertEquals(DateUtil.ceiling(date, DateField.MINUTE).getTime() + 1, DateUtils.ceiling(timestamp, MINUTE));
        assertEquals(DateUtil.truncate(date, DateField.MINUTE).getTime(), DateUtils.truncate(timestamp, MINUTE));

        //小时
        assertEquals(DateUtil.ceiling(date, DateField.HOUR_OF_DAY).getTime() + 1, DateUtils.ceiling(timestamp, HOUR_OF_DAY));
        assertEquals(DateUtil.truncate(date, DateField.HOUR_OF_DAY).getTime(), DateUtils.truncate(timestamp, HOUR_OF_DAY));

        //天
        assertEquals(DateUtil.ceiling(date, DateField.DAY_OF_YEAR).getTime() + 1, DateUtils.ceiling(timestamp, DAY_OF_YEAR));
        assertEquals(DateUtil.truncate(date, DateField.DAY_OF_YEAR).getTime(), DateUtils.truncate(timestamp, DAY_OF_YEAR));

        //周
        assertEquals(new DateTime(timestamp).weekOfWeekyear().roundCeilingCopy().getMillis(), DateUtils.ceiling(timestamp, WEEK_OF_YEAR));
        assertEquals(new DateTime(timestamp).weekOfWeekyear().roundFloorCopy().getMillis(), DateUtils.truncate(timestamp, WEEK_OF_YEAR));

        //月
        assertEquals(DateUtil.ceiling(date, DateField.MONTH).getTime() + 1, DateUtils.ceiling(timestamp, MONTH));
        assertEquals(DateUtil.truncate(date, DateField.MONTH).getTime(), DateUtils.truncate(timestamp, MONTH));

        //年
        assertEquals(DateUtil.ceiling(date, DateField.YEAR).getTime() + 1, DateUtils.ceiling(timestamp, YEAR));
        assertEquals(DateUtil.truncate(date, DateField.YEAR).getTime(), DateUtils.truncate(timestamp, YEAR));

    }

    /**
     * 测试季度
     */
    @Test
    void test4() {
        long currentTimeMills = 1674972486000L;

        //第一季度
        long truncate = DateUtils.truncate(currentTimeMills, -1);
        String truncateDateTime = DateUtils.formatDateTime(truncate);
        assertEquals("2023-01-01 00:00:00", truncateDateTime);

        long ceiling = DateUtils.ceiling(currentTimeMills, -1);
        String ceilingDateTime = DateUtils.formatDateTime(ceiling);
        assertEquals("2023-04-01 00:00:00", ceilingDateTime);

        //第二季度
        currentTimeMills = 1685340486000L;
        truncate = DateUtils.truncate(currentTimeMills, -1);
        truncateDateTime = DateUtils.formatDateTime(truncate);
        assertEquals("2023-04-01 00:00:00", truncateDateTime);

        ceiling = DateUtils.ceiling(currentTimeMills, -1);
        ceilingDateTime = DateUtils.formatDateTime(ceiling);
        assertEquals("2023-07-01 00:00:00", ceilingDateTime);

        //第三季度
        currentTimeMills = 1690610886000L;
        truncate = DateUtils.truncate(currentTimeMills, -1);
        truncateDateTime = DateUtils.formatDateTime(truncate);
        assertEquals("2023-07-01 00:00:00", truncateDateTime);

        ceiling = DateUtils.ceiling(currentTimeMills, -1);
        ceilingDateTime = DateUtils.formatDateTime(ceiling);
        assertEquals("2023-10-01 00:00:00", ceilingDateTime);

        //第四季度
        currentTimeMills = 1701238086000L;
        truncate = DateUtils.truncate(currentTimeMills, -1);
        truncateDateTime = DateUtils.formatDateTime(truncate);
        assertEquals("2023-10-01 00:00:00", truncateDateTime);

        ceiling = DateUtils.ceiling(currentTimeMills, -1);
        ceilingDateTime = DateUtils.formatDateTime(ceiling);
        assertEquals("2024-01-01 00:00:00", ceilingDateTime);
    }

}
