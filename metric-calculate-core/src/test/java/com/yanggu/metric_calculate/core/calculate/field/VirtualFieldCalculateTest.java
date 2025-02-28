package com.yanggu.metric_calculate.core.calculate.field;

import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactoryBase.AVIATOR_FUNCTION_FACTORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

/**
 * 虚拟字段单元测试类
 */
@ExtendWith(MockitoExtension.class)
class VirtualFieldCalculateTest {

    @Mock
    private MetricFieldProcessor<String> mockMetricFieldProcessor;

    @Mock
    private Map<String, Class<?>> fieldMap;

    @Test
    void init() {
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String express = "username";
        aviatorExpressParam.setExpress(express);

        MockedStatic<FieldProcessorUtil> fieldProcessorUtilMockedStatic = mockStatic(FieldProcessorUtil.class);
        AviatorFunctionFactory aviatorFunctionFactory = AVIATOR_FUNCTION_FACTORY;
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
    void process() {
        VirtualFieldCalculate<String> virtualFieldCalculate = new VirtualFieldCalculate<>();

        Map<String, Class<?>> tempFieldMap = new HashMap<>();
        tempFieldMap.put("tran_timestamp", Long.class);
        virtualFieldCalculate.setFieldMap(tempFieldMap);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("date_to_string(new java.util.Date(tran_timestamp), 'yyyy-MM-dd')");
        virtualFieldCalculate.setAviatorExpressParam(aviatorExpressParam);
        virtualFieldCalculate.setAviatorFunctionFactory(AVIATOR_FUNCTION_FACTORY);

        virtualFieldCalculate.init();

        Map<String, Object> input = new HashMap<>();
        long currentTimeMillis = System.currentTimeMillis();
        input.put("tran_timestamp", currentTimeMillis);
        String process = virtualFieldCalculate.process(input);
        assertEquals(DateUtil.format(new Date(currentTimeMillis), "yyyy-MM-dd"), process);
    }

    @Test
    void getColumnName() {
        VirtualFieldCalculate<String> virtualFieldCalculate = new VirtualFieldCalculate<>();
        virtualFieldCalculate.setColumnName("tran_date");
        assertEquals("tran_date", virtualFieldCalculate.getColumnName());
    }

}