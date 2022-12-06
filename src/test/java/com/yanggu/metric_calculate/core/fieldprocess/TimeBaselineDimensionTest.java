package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.client.magiccube.enums.TimeUnit;
import com.yanggu.metric_calculate.core.calculate.TimeWindow;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TimeBaselineDimensionTest {

    @Test
    public void getTimeWindow() {
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(7, TimeUnit.DAY);
        List<TimeWindow> timeWindow = timeBaselineDimension.getTimeWindow(System.currentTimeMillis());
        timeWindow.forEach(temp -> {
            System.out.println("窗口开始时间: " + DateUtil.formatDateTime(new Date(temp.getStart()))
                    + ", 窗口结束时间: " + DateUtil.formatDateTime(new Date(temp.getEnd())));
        });
    }
}