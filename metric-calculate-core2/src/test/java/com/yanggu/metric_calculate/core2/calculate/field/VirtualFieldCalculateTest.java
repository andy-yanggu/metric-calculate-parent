package com.yanggu.metric_calculate.core2.calculate.field;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactoryTest.getAviatorFunctionFactory;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

/**
 * 虚拟字段单元测试类
 */
@RunWith(MockitoJUnitRunner.class)
public class VirtualFieldCalculateTest {

    @Mock
    private MetricFieldProcessor<String> mockMetricFieldProcessor;

    @Mock
    private Map<String, Class<?>> fieldMap;

    private final String express = "username";

    @Test
    public void init() {
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(express);

        MockedStatic<FieldProcessorUtil> fieldProcessorUtilMockedStatic = Mockito.mockStatic(FieldProcessorUtil.class);
        AviatorFunctionFactory aviatorFunctionFactory = getAviatorFunctionFactory();
        fieldProcessorUtilMockedStatic.when(() -> FieldProcessorUtil.getMetricFieldProcessor(eq(fieldMap), eq(aviatorExpressParam), eq(aviatorFunctionFactory)))
                .thenReturn(mockMetricFieldProcessor);

        VirtualFieldCalculate<String> virtualFieldCalculate = new VirtualFieldCalculate<>();
        virtualFieldCalculate.setAviatorExpressParam(aviatorExpressParam);
        virtualFieldCalculate.setAviatorFunctionFactory(aviatorFunctionFactory);
        virtualFieldCalculate.setFieldMap(fieldMap);

        virtualFieldCalculate.init();
        assertEquals(mockMetricFieldProcessor, virtualFieldCalculate.getMetricFieldProcessor());
        assertEquals(aviatorExpressParam, virtualFieldCalculate.getAviatorExpressParam());
        assertEquals(fieldMap, virtualFieldCalculate.getFieldMap());
        assertEquals(aviatorFunctionFactory, virtualFieldCalculate.getAviatorFunctionFactory());

        fieldProcessorUtilMockedStatic.verify(() -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, aviatorExpressParam, aviatorFunctionFactory));
        fieldProcessorUtilMockedStatic.close();
    }

    @Test
    public void process() {
        VirtualFieldCalculate<String> virtualFieldCalculate = new VirtualFieldCalculate<>();

        Map<String, Class<?>> tempFieldMap = new HashMap<>();
        tempFieldMap.put("tran_timestamp", Long.class);
        virtualFieldCalculate.setFieldMap(tempFieldMap);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("date_to_string(new java.util.Date(tran_timestamp), 'yyyy-MM-dd')");
        virtualFieldCalculate.setAviatorExpressParam(aviatorExpressParam);
        virtualFieldCalculate.setAviatorFunctionFactory(getAviatorFunctionFactory());

        virtualFieldCalculate.init();

        JSONObject input = new JSONObject();
        long currentTimeMillis = System.currentTimeMillis();
        input.set("tran_timestamp", currentTimeMillis);
        String process = virtualFieldCalculate.process(input);
        assertEquals(DateUtil.format(new Date(currentTimeMillis), "yyyy-MM-dd"), process);
    }

    @Test
    public void getColumnName() {
        VirtualFieldCalculate<String> virtualFieldCalculate = new VirtualFieldCalculate<>();
        virtualFieldCalculate.setColumnName("tran_date");
        assertEquals("tran_date", virtualFieldCalculate.getColumnName());
    }

}