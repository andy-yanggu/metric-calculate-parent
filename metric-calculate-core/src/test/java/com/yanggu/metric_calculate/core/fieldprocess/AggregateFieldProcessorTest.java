package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.numeric.SumUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 聚合字段处理器单元测试类
 */
@RunWith(MockitoJUnitRunner.class)
public class AggregateFieldProcessorTest {

    @Mock
    private UnitFactory unitFactory;

    @Captor
    private ArgumentCaptor<String> aggregateTypeCaptor;

    @Captor
    private ArgumentCaptor<Double> resultCaptor;

    @Captor
    private ArgumentCaptor<Map<String, Object>> paramMapCaptor;

    /**
     * 调用super.init()方法
     * <p>直接调用MetricFieldProcessorTest测试类的init方法即可</p>
     *
     * @throws Exception
     */
    @Test
    public void testInit1() throws Exception {
        //AggregateFieldProcessor的init方法直接调用MetricFieldProcessor的init方法
        //直接调用MetricFieldProcessorTest测试类的init方法即可
        MetricFieldProcessorTest metricFieldProcessorTest = new MetricFieldProcessorTest();
        metricFieldProcessorTest.init1();
        metricFieldProcessorTest.init2();
        metricFieldProcessorTest.init3();
        metricFieldProcessorTest.init4();
        assertNotNull(metricFieldProcessorTest);
    }

    /**
     * 验证是否校验aggregateType
     *
     * @throws Exception
     */
    @Test
    public void testInit2() throws Exception {
        AggregateFieldProcessor aggregateFieldProcessor = new AggregateFieldProcessor();

        aggregateFieldProcessor.setMetricExpress("amount");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        aggregateFieldProcessor.setFieldMap(fieldMap);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, aggregateFieldProcessor::init);
        assertEquals("聚合类型为空", runtimeException.getMessage());
    }

    /**
     * 测试是否校验UnitFactory
     *
     * @throws Exception
     */
    @Test
    public void testInit3() throws Exception {
        AggregateFieldProcessor aggregateFieldProcessor = new AggregateFieldProcessor();

        aggregateFieldProcessor.setMetricExpress("amount");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        aggregateFieldProcessor.setFieldMap(fieldMap);
        aggregateFieldProcessor.setAggregateType("COUNT");

        RuntimeException runtimeException = assertThrows(RuntimeException.class, aggregateFieldProcessor::init);
        assertEquals("UnitFactory为空", runtimeException.getMessage());
    }

    /**
     * 测试init是否正常执行
     *
     * @throws Exception
     */
    @Test
    public void testInit4() throws Exception {
        AggregateFieldProcessor aggregateFieldProcessor = new AggregateFieldProcessor();

        aggregateFieldProcessor.setMetricExpress("amount");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        aggregateFieldProcessor.setFieldMap(fieldMap);
        aggregateFieldProcessor.setAggregateType("COUNT");
        aggregateFieldProcessor.setUnitFactory(unitFactory);

        aggregateFieldProcessor.init();

        assertEquals("COUNT", aggregateFieldProcessor.getAggregateType());
        assertEquals(unitFactory, aggregateFieldProcessor.getUnitFactory());
    }

    /**
     * 传入的明细数据中没有对应的度量数据应该返回null
     *
     * @throws Exception
     */
    @Test
    public void testProcess1() throws Exception {
        AggregateFieldProcessor aggregateFieldProcessor = new AggregateFieldProcessor();

        aggregateFieldProcessor.setMetricExpress("amount");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        aggregateFieldProcessor.setFieldMap(fieldMap);
        aggregateFieldProcessor.setAggregateType("COUNT");
        aggregateFieldProcessor.setUnitFactory(unitFactory);

        aggregateFieldProcessor.init();

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount2", "100");

        MergedUnit process = aggregateFieldProcessor.process(jsonObject);
        assertNull(process);
    }

    /**
     * 测试正常流程, UnitFactory返回正常数据
     *
     * @throws Exception
     */
    @Test
    public void testProcess2() throws Exception {
        String aggregateType = "SUM";
        String amount = "amount";
        String value = "1.0";

        //构造AggregateFieldProcessor
        AggregateFieldProcessor aggregateFieldProcessor = new AggregateFieldProcessor();
        aggregateFieldProcessor.setMetricExpress(amount);
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put(amount, Double.class);
        aggregateFieldProcessor.setFieldMap(fieldMap);
        aggregateFieldProcessor.setAggregateType(aggregateType);
        aggregateFieldProcessor.setUnitFactory(unitFactory);
        aggregateFieldProcessor.init();

        //mock unitFactory的返回值
        SumUnit sumUnit = new SumUnit<>(CubeDecimal.of(value));
        when(unitFactory.initInstanceByValue(eq(aggregateType), eq(Double.valueOf(value)), isNull())).thenReturn(sumUnit);

        //构造明细数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.set(amount, value);

        //执行process方法
        MergedUnit mergedUnit = aggregateFieldProcessor.process(jsonObject);

        //验证数据
        assertEquals(sumUnit, mergedUnit);

        //进行参数捕获
        verify(unitFactory).initInstanceByValue(aggregateTypeCaptor.capture(), resultCaptor.capture(), paramMapCaptor.capture());
        //验证参数是否正确
        assertEquals(aggregateType, aggregateTypeCaptor.getValue());
        assertEquals(Double.parseDouble(value), resultCaptor.getValue(), Double.MIN_VALUE);
        assertNull(paramMapCaptor.getValue());
    }

}