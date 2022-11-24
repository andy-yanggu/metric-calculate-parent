package com.yanggu.metriccalculate.fieldprocess;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 时间字段处理器
 */
public class TimeFieldProcessorTest {

    /**
     * 时间格式是时间戳
     */
    @Test
    public void process1() {
        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor("timestamp", "tran_timestamp");

        JSONObject jsonObject = new JSONObject();
        long currentTimeMillis = System.currentTimeMillis();
        jsonObject.set("tran_timestamp", currentTimeMillis);

        Long process = timeFieldProcessor.process(jsonObject);
        assertEquals(currentTimeMillis, process.longValue());
    }

    /**
     * 时间格式是yyyyMMdd
     */
    @Test
    public void process2() {

        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor("yyyyMMdd", "tran_date");

        String date = "20221124";
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("tran_date", date);

        Long process = timeFieldProcessor.process(jsonObject);
        assertEquals(DateUtil.parse(date).getTime(), process.longValue());
    }

}