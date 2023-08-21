package com.yanggu.metric_calculate.core.field_process.time;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * 时间字段处理器
 */
class TimeFieldProcessorTest {

    /**
     * 测试是否校验时间字段
     */
    @Test
    void init1() {
        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor(null, null);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, timeFieldProcessor::init);
        assertEquals("时间格式为空", runtimeException.getMessage());
    }

    /**
     * 测试是否校验时间格式
     */
    @Test
    void init2() {
        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor("TimeColumnName", null);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, timeFieldProcessor::init);
        assertEquals("时间字段的值为空", runtimeException.getMessage());
    }

    /**
     * 测试正常流程
     */
    @Test
    void init3() {
        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor("timeFormat", "TimeColumnName");
        timeFieldProcessor.init();

        assertEquals("timeFormat", timeFieldProcessor.getTimeFormat());
        assertEquals("TimeColumnName", timeFieldProcessor.getTimeColumnName());
    }

    /**
     * 时间格式是时间戳
     */
    @Test
    void process1() {
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
    void process2() {

        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor("yyyyMMdd", "tran_date");

        String date = "20221124";
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("tran_date", date);

        Long process = timeFieldProcessor.process(jsonObject);
        assertEquals(DateUtil.parse(date).getTime(), process.longValue());
    }

}