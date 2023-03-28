package com.yanggu.metric_calculate.core2.test;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

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
        DateTime dateTime = new DateTime().hourOfDay().roundFloorCopy();;
        String print = dateTimeFormatter.print(dateTime.getMillis());
        System.out.println("print = " + print);
    }

}
