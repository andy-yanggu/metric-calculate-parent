package com.yanggu.metric_calculate.core.field_process.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessorTest;
import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.numeric.CountUnit;
import com.yanggu.metric_calculate.core.unit.numeric.CovUnit;
import com.yanggu.metric_calculate.core.unit.numeric.SumUnit;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static com.yanggu.metric_calculate.core.unit.UnitFactoryTest.getTestUnitFactory;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

/**
 * AggregateNumberFieldProcessor单元测试类
 * <p>数值型聚合字段处理器单元测试类</p>
 */
@RunWith(MockitoJUnitRunner.class)
public class AggregateNumberFieldProcessorTest {

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
        AggregateNumberFieldProcessor<JSONObject, SumUnit<CubeDecimal>> aggregateNumberFieldProcessor = new AggregateNumberFieldProcessor<>();

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        aggregateNumberFieldProcessor.setFieldMap(fieldMap);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, aggregateNumberFieldProcessor::init);
        assertEquals("聚合类型为空", runtimeException.getMessage());
    }

    /**
     * 测试是否校验UnitFactory
     *
     * @throws Exception
     */
    @Test
    public void testInit3() throws Exception {
        AggregateNumberFieldProcessor<JSONObject, CountUnit> aggregateNumberFieldProcessor = new AggregateNumberFieldProcessor<>();

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        aggregateNumberFieldProcessor.setFieldMap(fieldMap);
        aggregateNumberFieldProcessor.setAggregateType("COUNT");

        RuntimeException runtimeException = assertThrows(RuntimeException.class, aggregateNumberFieldProcessor::init);
        assertEquals("UnitFactory为空", runtimeException.getMessage());
    }

    /**
     * 测试是否校验mergeUnitClazz
     *
     * @throws Exception
     */
    @Test
    public void testInit4() throws Exception {
        AggregateNumberFieldProcessor<JSONObject, CountUnit> aggregateNumberFieldProcessor = new AggregateNumberFieldProcessor<>();

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        aggregateNumberFieldProcessor.setFieldMap(fieldMap);
        aggregateNumberFieldProcessor.setAggregateType("COUNT");

        aggregateNumberFieldProcessor.setUnitFactory(getTestUnitFactory());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, aggregateNumberFieldProcessor::init);
        assertEquals("需要设置mergeUnitClazz", runtimeException.getMessage());
    }

    /**
     * 测试init是否正常执行
     *
     * @throws Exception
     */
    @Test
    public void testInit5() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        String aggregateType = "SUM";
        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setMetricExpress("amount");
        udafParam.setAggregateType(aggregateType);

        doReturn(SumUnit.class).when(unitFactory).getMergeableClass(aggregateType);
        AggregateNumberFieldProcessor<JSONObject, SumUnit<CubeDecimal>> aggregateNumberFieldProcessor =
                (AggregateNumberFieldProcessor<JSONObject, SumUnit<CubeDecimal>>)
                        FieldProcessorUtil.<JSONObject, SumUnit<CubeDecimal>>getBaseAggregateFieldProcessor(Collections.singletonList(udafParam), unitFactory, fieldMap);


        assertEquals(aggregateType, aggregateNumberFieldProcessor.getAggregateType());
        assertEquals(unitFactory, aggregateNumberFieldProcessor.getUnitFactory());
        assertEquals(SumUnit.class, aggregateNumberFieldProcessor.getMergeUnitClazz());
        assertNotNull(aggregateNumberFieldProcessor.getMetricFieldProcessor());
    }

    /**
     * 传入的明细数据中没有对应的度量数据应该返回null
     *
     * @throws Exception
     */
    @Test
    public void testProcess1() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        String aggregateType = "SUM";
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setMetricExpress("amount");
        baseUdafParam.setAggregateType(aggregateType);


        doReturn(SumUnit.class).when(unitFactory).getMergeableClass(aggregateType);
        BaseAggregateFieldProcessor<JSONObject, ?> aggregateNumberFieldProcessor =
                        FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(baseUdafParam), unitFactory, fieldMap);

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount2", "100");

        MergedUnit<?> process = aggregateNumberFieldProcessor.process(jsonObject);
        assertNull(process);
    }

    /**
     * 测试正常流程, UnitFactory返回正常数据
     *
     * @throws Exception
     */
    @Test
    public void testProcessWithMock() throws Exception {
        String aggregateType = "SUM";
        String amount = "amount";
        String value = "1.0";

        //构造AggregateFieldProcessor
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put(amount, Double.class);

        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setMetricExpress(amount);
        baseUdafParam.setAggregateType(aggregateType);

        doReturn(SumUnit.class).when(unitFactory).getMergeableClass(aggregateType);
        AggregateNumberFieldProcessor<JSONObject, SumUnit<CubeDecimal>> aggregateNumberFieldProcessor =
                (AggregateNumberFieldProcessor<JSONObject, SumUnit<CubeDecimal>>)
                        FieldProcessorUtil.<JSONObject, SumUnit<CubeDecimal>>getBaseAggregateFieldProcessor(Collections.singletonList(baseUdafParam), unitFactory, fieldMap);

        //mock unitFactory的返回值
        SumUnit<CubeDecimal> sumUnit = new SumUnit<>(CubeDecimal.of(value));
        when(unitFactory.initInstanceByValue(eq(aggregateType), eq(Double.valueOf(value)), isNull())).thenReturn(sumUnit);

        //构造明细数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.set(amount, value);

        //执行process方法
        SumUnit<CubeDecimal> mergedUnit = aggregateNumberFieldProcessor.process(jsonObject);

        //验证数据
        assertEquals(sumUnit, mergedUnit);

        //进行参数捕获
        verify(unitFactory).initInstanceByValue(aggregateTypeCaptor.capture(), resultCaptor.capture(), paramMapCaptor.capture());
        //验证参数是否正确
        assertEquals(aggregateType, aggregateTypeCaptor.getValue());
        assertEquals(Double.parseDouble(value), resultCaptor.getValue(), Double.MIN_VALUE);
        assertNull(paramMapCaptor.getValue());
    }

    /**
     * 测试协方差CovUnit
     * @throws Exception
     */
    @Test
    public void testProcess_CovUnit() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("COV");
        baseUdafParam.setMetricExpressList(Arrays.asList("amount", "amount1"));
        List<BaseUdafParam> baseUdafParamList = Collections.singletonList(baseUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);
        fieldMap.put("amount1", Integer.class);

        BaseAggregateFieldProcessor<JSONObject, CovUnit<CubeLong>> numberAggregateFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(baseUdafParamList, getTestUnitFactory(), fieldMap);

        JSONObject input = new JSONObject();
        input.set("amount", 1);
        input.set("amount1", 2);

        CovUnit<CubeLong> process = numberAggregateFieldProcessor.process(input);
        Number value = process.value();
        assertEquals(0, value);
    }

}