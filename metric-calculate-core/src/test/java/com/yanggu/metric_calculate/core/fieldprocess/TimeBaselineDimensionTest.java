package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.core.pojo.metric.TimeWindow;
import com.yanggu.metric_calculate.core.enums.TimeUnit;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class TimeBaselineDimensionTest {

    @Test
    public void getTimeWindow() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(7, TimeUnit.DAY);
        List<TimeWindow> timeWindow = timeBaselineDimension.getTimeWindow(System.currentTimeMillis());
        timeWindow.forEach(temp -> {
            System.out.println("窗口开始时间: " + DateUtil.formatDateTime(new Date(temp.getWindowStart()))
                    + ", 窗口结束时间: " + DateUtil.formatDateTime(new Date(temp.getWindowEnd())));
        });
    }

    @Test
    public void test2() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(7, TimeUnit.MILLS);
        DateTime parse = DateUtil.parse("2022-02-03 12:00:00");
        Long timestamp = timeBaselineDimension.getCurrentAggregateTimestamp(parse.getTime());
        System.out.println(DateUtil.formatDateTime(new Date(timestamp)));
    }

}