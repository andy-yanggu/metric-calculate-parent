package com.yanggu.metric_calculate.core.util;

import com.yanggu.metric_calculate.core.field_process.filter.FilterFieldProcessor;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldProcessorUtilTest {

    @Test
    public void testGetFilterFieldProcessor() {
        FilterFieldProcessor<Object> filterFieldProcessor = FieldProcessorUtil.getFilterFieldProcessor(null, null);

    }

    @Test
    public void getTimeFieldProcessor() {
    }

    @Test
    public void getDimensionSetProcessor() {
    }

    @Test
    public void getMetricFieldProcessor() {
    }

    @Test
    public void getDistinctFieldFieldProcessor() {
    }

    @Test
    public void getOrderFieldProcessor() {
    }

    @Test
    public void getBaseAggregateFieldProcessor() {
    }

    @Test
    public void getAggregateMapUnitFieldProcessor() {
    }

    @Test
    public void getAggregateMixUnitFieldProcessor() {
    }

    @Test
    public void getAggregateFieldProcessor() {
    }
}