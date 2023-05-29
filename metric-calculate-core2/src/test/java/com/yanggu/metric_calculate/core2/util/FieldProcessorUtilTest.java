package com.yanggu.metric_calculate.core2.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.mutable.MutableObj;
import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.exception.ExpressionSyntaxErrorException;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core2.aggregate_function.collection.ListObjectAggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.map.BaseMapAggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.mix.BaseMixAggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.object.FirstObjectAggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.Numerical;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.aggregate.*;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric_list.MetricListFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.AggregateFunctionParam;
import com.yanggu.metric_calculate.core2.pojo.metric.Dimension;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeColumn;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MapUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MixUdafParam;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactoryTest.getAggregateFunctionFactory;
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
        assertThrows(ExpressionSyntaxErrorException.class, () -> FieldProcessorUtil.getFilterFieldProcessor(fieldMap, filterExpress));
    }

    @Test
    public void testGetTimeFieldProcessor() {
        TimeColumn timeColumn = new TimeColumn("time", "HH:mm:ss");
        TimeFieldProcessor timeFieldProcessor = FieldProcessorUtil.getTimeFieldProcessor(timeColumn);
        assertNotNull(timeFieldProcessor);
        assertEquals("time", timeFieldProcessor.getTimeColumnName());
        assertEquals("HH:mm:ss", timeFieldProcessor.getTimeFormat());
    }

    @Test
    public void testGetDimensionSetProcessor_Positive() {
        String key = "key";
        String metricName = "metricName";
        List<Dimension> dimensionList = new ArrayList<>();
        DimensionSetProcessor dimensionSetProcessor = FieldProcessorUtil.getDimensionSetProcessor(key, metricName, dimensionList);
        assertNotNull(dimensionSetProcessor);
        assertEquals(key, dimensionSetProcessor.getKey());
        assertEquals(metricName, dimensionSetProcessor.getMetricName());
        assertEquals(dimensionList, dimensionSetProcessor.getDimensionList());
    }

    @Test
    public void getMetricFieldProcessor_positiveTestCase() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("field1", Integer.class);
        fieldMap.put("field2", Integer.class);
        String metricExpress = "field1 + field2";
        MetricFieldProcessor<Integer> metricFieldProcessor = FieldProcessorUtil.getMetricFieldProcessor(fieldMap, metricExpress);
        assertNotNull(metricFieldProcessor);
        assertEquals(fieldMap, metricFieldProcessor.getFieldMap());
        assertEquals(metricExpress, metricFieldProcessor.getMetricExpress());
        Expression expression = AviatorEvaluator.compile(metricExpress, true);
        assertEquals(expression, metricFieldProcessor.getMetricExpression());
    }

    @Test
    public void testGetMetricListFieldProcessor() {
        // Positive Test Case
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("metric1", Integer.class);
        fieldMap.put("metric2", Integer.class);
        List<String> metricExpressList = Arrays.asList("metric1", "metric2");
        MetricListFieldProcessor metricListFieldProcessor = FieldProcessorUtil.getMetricListFieldProcessor(fieldMap, metricExpressList);
        assertNotNull(metricListFieldProcessor);
        assertEquals(fieldMap, metricListFieldProcessor.getFieldMap());
        assertEquals(metricExpressList, metricListFieldProcessor.getMetricExpressList());
        Expression expression1 = AviatorEvaluator.compile("metric1", true);
        Expression expression2 = AviatorEvaluator.compile("metric2", true);

        assertEquals(expression1, metricListFieldProcessor.getMetricFieldProcessorList().get(0).getMetricExpression());
        assertEquals(expression2, metricListFieldProcessor.getMetricFieldProcessorList().get(1).getMetricExpression());
    }

    @Test
    public void testGetDistinctFieldFieldProcessor_Positive() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("field1", String.class);
        fieldMap.put("field2", Integer.class);
        List<String> distinctFieldList = Arrays.asList("field1", "field2");
        MultiFieldDistinctFieldProcessor processor = FieldProcessorUtil.getDistinctFieldFieldProcessor(fieldMap, distinctFieldList);
        assertNotNull(processor);
        assertEquals(fieldMap, processor.getFieldMap());
        assertEquals(distinctFieldList, processor.getDistinctFieldList());
        Expression expression1 = AviatorEvaluator.compile("field1", true);
        assertEquals(expression1, processor.getMetricFieldProcessorList().get(0).getMetricExpression());
        Expression expression2 = AviatorEvaluator.compile("field2", true);
        assertEquals(expression2, processor.getMetricFieldProcessorList().get(1).getMetricExpression());
    }

    @Test
    public void testGetOrderFieldProcessor() {
        // Positive Test Case
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("field1", String.class);
        fieldMap.put("field2", Integer.class);

        List<FieldOrderParam> fieldOrderParamList = new ArrayList<>();
        FieldOrderParam field1 = new FieldOrderParam("field1", true);
        FieldOrderParam field2 = new FieldOrderParam("field2", true);
        fieldOrderParamList.add(field1);
        fieldOrderParamList.add(field2);

        MultiFieldOrderFieldProcessor tempMultiFieldOrderFieldProcessor = FieldProcessorUtil.getOrderFieldProcessor(fieldMap, fieldOrderParamList);

        assertNotNull(tempMultiFieldOrderFieldProcessor);
        assertEquals(fieldMap, tempMultiFieldOrderFieldProcessor.getFieldMap());
        assertEquals(fieldOrderParamList, tempMultiFieldOrderFieldProcessor.getFieldOrderParamList());
        Expression expression1 = AviatorEvaluator.compile("field1", true);
        Expression expression2 = AviatorEvaluator.compile("field2", true);
        assertEquals(expression1, tempMultiFieldOrderFieldProcessor.getMetricFieldProcessorList().get(0).getMetricExpression());
        assertEquals(expression2, tempMultiFieldOrderFieldProcessor.getMetricFieldProcessorList().get(1).getMetricExpression());
    }

    @Test
    public void testGetMixFieldProcessor() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("id", Integer.class);
        fieldMap.put("amount", String.class);

        MixUdafParam mixUdafParam = new MixUdafParam();
        mixUdafParam.setAggregateType("BASEMIX");
        Map<String, BaseUdafParam> mixAggMap = new HashMap<>();
        mixUdafParam.setMixAggMap(mixAggMap);
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SUM");
        baseUdafParam.setMetricExpress("amount");
        mixAggMap.put("SUM", baseUdafParam);

        MixFieldProcessor<Object> mixFieldProcessor = FieldProcessorUtil.getMixFieldProcessor(fieldMap, mixUdafParam, getAggregateFunctionFactory());

        assertEquals(fieldMap, mixFieldProcessor.getFieldMap());
        assertEquals(mixUdafParam, mixFieldProcessor.getMixUdafParam());
        assertEquals(getAggregateFunctionFactory(), mixFieldProcessor.getAggregateFunctionFactory());
        Map<String, FieldProcessor<JSONObject, Object>> multiBaseAggProcessorMap = mixFieldProcessor.getMultiBaseAggProcessorMap();
        assertEquals(1, multiBaseAggProcessorMap.size());
        assertEquals(FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory()), multiBaseAggProcessorMap.get("SUM"));
    }

    @Test
    public void testGetMapFieldProcessor_Positive() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("amount", Integer.class);

        AggregateFunctionFactory factory = getAggregateFunctionFactory();

        MapUdafParam mapUdafParam = new MapUdafParam();
        mapUdafParam.setDistinctFieldList(CollUtil.toList("name"));

        BaseUdafParam valueAggParam = new BaseUdafParam();
        valueAggParam.setMetricExpress("amount");
        valueAggParam.setAggregateType("SUM");
        mapUdafParam.setValueAggParam(valueAggParam);

        MapFieldProcessor<Pair<MultiFieldDistinctKey, Integer>> mapFieldProcessor = FieldProcessorUtil.getMapFieldProcessor(fieldMap, factory, mapUdafParam);

        assertNotNull(mapFieldProcessor);
        assertEquals(fieldMap, mapFieldProcessor.getFieldMap());
        assertEquals(mapUdafParam, mapFieldProcessor.getMapUdafParam());
        assertEquals(factory, mapFieldProcessor.getAggregateFunctionFactory());

        assertEquals(FieldProcessorUtil.getDistinctFieldFieldProcessor(fieldMap, mapUdafParam.getDistinctFieldList()), mapFieldProcessor.getKeyFieldProcessor());
        assertEquals(FieldProcessorUtil.getBaseFieldProcessor(valueAggParam, fieldMap, getAggregateFunctionFactory()), mapFieldProcessor.getValueAggregateFieldProcessor());
    }

    /**
     * 数值类型
     */
    @Test
    public void testNumberGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setCalculateLogic("SUM");
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SUM");
        baseUdafParam.setMetricExpress("amount");
        aggregateFunctionParam.setBaseUdafParam(baseUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);

        AggregateFunctionFactory factory = getAggregateFunctionFactory();
        AggregateFieldProcessor<Integer, Double, Double> aggregateFieldProcessor = FieldProcessorUtil.getAggregateFieldProcessor(aggregateFunctionParam, fieldMap, factory);

        FieldProcessor<JSONObject, Integer> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());
        assertEquals(baseFieldProcessor, aggregateFieldProcessor.getFieldProcessor());
        assertEquals(SumAggregateFunction.class, aggregateFieldProcessor.getAggregateFunction().getClass());
    }

    /**
     * 对象类型
     */
    @Test
    public void testObjectGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setCalculateLogic("FIRSTOBJECT");
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("FIRSTOBJECT");
        aggregateFunctionParam.setBaseUdafParam(baseUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);

        AggregateFunctionFactory factory = getAggregateFunctionFactory();
        AggregateFieldProcessor<JSONObject, MutableObj<JSONObject>, JSONObject> aggregateFieldProcessor = FieldProcessorUtil.getAggregateFieldProcessor(aggregateFunctionParam, fieldMap, factory);

        FieldProcessor<JSONObject, JSONObject> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());
        assertEquals(baseFieldProcessor, aggregateFieldProcessor.getFieldProcessor());
        assertEquals(FirstObjectAggregateFunction.class, aggregateFieldProcessor.getAggregateFunction().getClass());
    }

    /**
     * 集合类型
     */
    @Test
    public void testCollectionGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setCalculateLogic("LISTOBJECT");
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("LISTOBJECT");
        aggregateFunctionParam.setBaseUdafParam(baseUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);

        AggregateFunctionFactory factory = getAggregateFunctionFactory();
        AggregateFieldProcessor<JSONObject, List<JSONObject>, List<JSONObject>> aggregateFieldProcessor = FieldProcessorUtil.getAggregateFieldProcessor(aggregateFunctionParam, fieldMap, factory);

        FieldProcessor<JSONObject, JSONObject> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());
        assertEquals(baseFieldProcessor, aggregateFieldProcessor.getFieldProcessor());
        assertEquals(new ListObjectAggregateFunction<JSONObject>(), aggregateFieldProcessor.getAggregateFunction());
    }

    /**
     * 映射类型
     */
    @Test
    public void testMapGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setCalculateLogic("BASEMAP");

        MapUdafParam mapUdafParam = new MapUdafParam();
        mapUdafParam.setAggregateType("BASEMAP");
        mapUdafParam.setDistinctFieldList(CollUtil.toList("name"));

        BaseUdafParam valueAggParam = new BaseUdafParam();
        valueAggParam.setAggregateType("SUM");
        valueAggParam.setMetricExpress("amount");

        mapUdafParam.setValueAggParam(valueAggParam);
        aggregateFunctionParam.setMapUdafParam(mapUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("amount", Integer.class);

        AggregateFunctionFactory factory = getAggregateFunctionFactory();
        AggregateFieldProcessor<Pair<MultiFieldDistinctKey, Integer>, Map<MultiFieldDistinctKey, Double>, Map<MultiFieldDistinctKey, Double>> aggregateFieldProcessor = FieldProcessorUtil.getAggregateFieldProcessor(aggregateFunctionParam, fieldMap, factory);

        FieldProcessor<JSONObject, Pair<MultiFieldDistinctKey, Integer>> baseFieldProcessor = FieldProcessorUtil.getMapFieldProcessor(fieldMap, factory, mapUdafParam);
        assertEquals(baseFieldProcessor, aggregateFieldProcessor.getFieldProcessor());

        BaseMapAggregateFunction<MultiFieldDistinctKey, Integer, Double, Double> baseMapAggregateFunction = new BaseMapAggregateFunction<>();
        baseMapAggregateFunction.setValueAggregateFunction(new SumAggregateFunction<>());
        assertEquals(baseMapAggregateFunction, aggregateFieldProcessor.getAggregateFunction());
    }

    /**
     * 混合类型
     */
    @Test
    public void testMixGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setCalculateLogic("BASEMIX");

        MixUdafParam mixUdafParam = new MixUdafParam();
        mixUdafParam.setAggregateType("BASEMIX");

        HashMap<String, BaseUdafParam> mixAggMap = new HashMap<>();
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SUM");
        baseUdafParam.setMetricExpress("amount");
        mixAggMap.put("amount", baseUdafParam);
        mixUdafParam.setMixAggMap(mixAggMap);

        mixUdafParam.setExpress("amount");
        aggregateFunctionParam.setMixUdafParam(mixUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);

        AggregateFunctionFactory factory = getAggregateFunctionFactory();

        AggregateFieldProcessor<Map<String, Object>, Map<String, Object>, Double> aggregateFieldProcessor = FieldProcessorUtil.getAggregateFieldProcessor(aggregateFunctionParam, fieldMap, factory);

        MixFieldProcessor<Map<String, Object>> mixFieldProcessor = FieldProcessorUtil.getMixFieldProcessor(fieldMap, mixUdafParam, factory);
        assertEquals(mixFieldProcessor, aggregateFieldProcessor.getFieldProcessor());

        BaseMixAggregateFunction<Double> baseMixAggregateFunction = new BaseMixAggregateFunction<>();
        Map<String, AggregateFunction> mixAggregateFunctionMap = new HashMap<>();
        mixAggregateFunctionMap.put("amount", new SumAggregateFunction<Integer>());
        baseMixAggregateFunction.setMixAggregateFunctionMap(mixAggregateFunctionMap);
        baseMixAggregateFunction.setExpression(AviatorEvaluator.compile("amount", true));
        assertEquals(baseMixAggregateFunction, aggregateFieldProcessor.getAggregateFunction());
    }

    @Test
    public void testInvalidGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setCalculateLogic("invalid");

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);

        AggregateFunctionFactory factory = Mockito.mock(AggregateFunctionFactory.class);
        AggregateFunction<Object, Object, Object> aggregateFunction = Mockito.mock(AggregateFunction.class);
        Mockito.when(factory.getAggregateFunction("invalid")).thenReturn(aggregateFunction);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> FieldProcessorUtil.getAggregateFieldProcessor(aggregateFunctionParam, fieldMap, factory));
        assertEquals("暂不支持聚合类型: " + aggregateFunction.getClass().getName(), runtimeException.getMessage());
    }

    @Test
    public void getBaseFieldProcessor_Numerical_Test() {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setMetricExpress("test1");
        baseUdafParam.setAggregateType("SUM");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("test1", Double.class);
        FieldProcessor<JSONObject, Double> fieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());
        assertTrue(fieldProcessor instanceof NumberFieldProcessor);
        NumberFieldProcessor<Double> numberFieldProcessor = (NumberFieldProcessor<Double>) fieldProcessor;
        assertEquals(baseUdafParam, numberFieldProcessor.getUdafParam());
        assertEquals(fieldMap, numberFieldProcessor.getFieldMap());
        assertEquals(SumAggregateFunction.class.getAnnotation(Numerical.class), numberFieldProcessor.getNumerical());
        assertEquals(AviatorEvaluator.compile("test1", true), numberFieldProcessor.getMetricFieldProcessor().getMetricExpression());
        assertNull(numberFieldProcessor.getMetricListFieldProcessor());
    }

    @Test
    public void getBaseFieldProcessor_Objective_Test() {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("FIRSTFIELD");
        baseUdafParam.setRetainExpress("field1");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("field1", String.class);
        fieldMap.put("field2", Integer.class);
        FieldProcessor<JSONObject, String> fieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());
        assertTrue(fieldProcessor instanceof ObjectFieldProcessor);
    }

    @Test
    public void getBaseFieldProcessor_Collective_Test() {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("LISTOBJECT");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        FieldProcessor<JSONObject, JSONObject> fieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());
        assertTrue(fieldProcessor instanceof CollectionFieldProcessor);
    }

    @Test
    public void getBaseFieldProcessor_Invalid_Test() {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("BASEMIX");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory()));
        assertEquals("不支持的聚合类型: BASEMIX", runtimeException.getMessage());
    }

}