package com.yanggu.metric_calculate.core2.test;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.core2.util.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.util.Date;

import static java.util.Calendar.*;
import static org.junit.Assert.assertEquals;

public class JodaTimeTest {

    /**
     * format and parse
     */
    @Test
    public void test1() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        long millis = dateTimeFormatter.parseMillis("2023-03-28 15:25:28");
        System.out.println("millis = " + millis);

        String print = dateTimeFormatter.print(millis);
        System.out.println("print = " + print);
    }

    /**
     * ceiling
     */
    @Test
    public void test2() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = new DateTime().hourOfDay().roundCeilingCopy();
        String print = dateTimeFormatter.print(dateTime.getMillis());
        System.out.println("print = " + print);
    }

    /**
     * truncate
     */
    @Test
    public void test3() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = new DateTime().hourOfDay().roundFloorCopy();
        String print = dateTimeFormatter.print(dateTime.getMillis());
        System.out.println("print = " + print);
    }

    @Test
    public void test4() {
        long ceiling = DateUtils.ceiling(System.currentTimeMillis(), HOUR_OF_DAY);
        long millis = new DateTime().hourOfDay().roundCeilingCopy().getMillis();
        assertEquals(ceiling, millis);

        long truncate = DateUtils.truncate(System.currentTimeMillis(), HOUR_OF_DAY);
        long millis1 = new DateTime().hourOfDay().roundFloorCopy().getMillis();
        assertEquals(truncate, millis1);
    }

    @Test
    public void test5() {
        long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        assertEquals(DateUtil.ceiling(date, DateField.MILLISECOND).getTime(), DateUtils.ceiling(timestamp, MILLISECOND));
        assertEquals(DateUtil.truncate(date, DateField.MILLISECOND).getTime(), DateUtils.truncate(timestamp, MILLISECOND));

        assertEquals(DateUtil.ceiling(date, DateField.SECOND).getTime() + 1, DateUtils.ceiling(timestamp, SECOND));
        assertEquals(DateUtil.truncate(date, DateField.SECOND).getTime(), DateUtils.truncate(timestamp, SECOND));

        assertEquals(DateUtil.ceiling(date, DateField.MINUTE).getTime() + 1, DateUtils.ceiling(timestamp, MINUTE));
        assertEquals(DateUtil.truncate(date, DateField.MINUTE).getTime(), DateUtils.truncate(timestamp, MINUTE));

        assertEquals(DateUtil.ceiling(date, DateField.HOUR_OF_DAY).getTime() + 1, DateUtils.ceiling(timestamp, HOUR_OF_DAY));
        assertEquals(DateUtil.truncate(date, DateField.HOUR_OF_DAY).getTime(), DateUtils.truncate(timestamp, HOUR_OF_DAY));

        assertEquals(DateUtil.ceiling(date, DateField.DAY_OF_YEAR).getTime() + 1, DateUtils.ceiling(timestamp, DAY_OF_YEAR));
        assertEquals(DateUtil.truncate(date, DateField.DAY_OF_YEAR).getTime(), DateUtils.truncate(timestamp, DAY_OF_YEAR));

        //assertEquals(DateUtil.ceiling(date, DateField.WEEK_OF_YEAR).getTime() + 1, DateUtils.ceiling(timestamp, WEEK_OF_YEAR));
        //assertEquals(DateUtil.truncate(date, DateField.WEEK_OF_YEAR).getTime(), DateUtils.truncate(timestamp, WEEK_OF_YEAR));

        assertEquals(DateUtil.ceiling(date, DateField.MONTH).getTime() + 1, DateUtils.ceiling(timestamp, MONTH));
        assertEquals(DateUtil.truncate(date, DateField.MONTH).getTime(), DateUtils.truncate(timestamp, MONTH));

        assertEquals(DateUtil.ceiling(date, DateField.YEAR).getTime() + 1, DateUtils.ceiling(timestamp, YEAR));
        assertEquals(DateUtil.truncate(date, DateField.YEAR).getTime(), DateUtils.truncate(timestamp, YEAR));

    }

    @Test
    public void test6() {
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
