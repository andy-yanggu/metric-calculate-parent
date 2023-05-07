package com.yanggu.metric_calculate.core2.util;

import com.googlecode.aviator.exception.ExpressionSyntaxErrorException;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeColumn;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FieldProcessorUtilTest {

    @Test
    public void testGetFilterFieldProcessor_Positive() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("age", Integer.class);
        String filterExpress = "name == 'John'";
        FilterFieldProcessor filterFieldProcessor = FieldProcessorUtil.getFilterFieldProcessor(fieldMap, filterExpress);
        assertNotNull(filterFieldProcessor);
    }

    @Test
    public void testGetFilterFieldProcessor_Negative() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("age", Integer.class);
        String filterExpress = "invalid filter expression";
        assertThrows(ExpressionSyntaxErrorException.class,
                () -> FieldProcessorUtil.getFilterFieldProcessor(fieldMap, filterExpress));
    }

    @Test
    public void testGetTimeFieldProcessor() {
        TimeColumn timeColumn = new TimeColumn("time", "HH:mm:ss");
        TimeFieldProcessor timeFieldProcessor = FieldProcessorUtil.getTimeFieldProcessor(timeColumn);
        assertNotNull(timeFieldProcessor);
        assertEquals("time", timeFieldProcessor.getTimeColumnName());
        assertEquals("HH:mm:ss", timeFieldProcessor.getTimeFormat());
    }

}