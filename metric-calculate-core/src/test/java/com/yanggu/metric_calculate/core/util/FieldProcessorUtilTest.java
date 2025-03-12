package com.yanggu.metric_calculate.core.util;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import com.yanggu.metric_calculate.core.aggregate_function.collection.ListObjectAggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.map.BaseMapAggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.mix.BaseMixAggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.object.FirstObjectAggregateFunction;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.aggregate.*;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.metric_list.MetricListFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.multi_field.MultiFieldDataFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelDimensionColumn;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelTimeColumn;
import com.yanggu.metric_calculate.core.pojo.udaf_param.*;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.*;
import static com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactoryBase.AGGREGATE_FUNCTION_FACTORY;
import static com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactoryBase.AVIATOR_FUNCTION_FACTORY;
import static org.junit.jupiter.api.Assertions.*;

class FieldProcessorUtilTest {

    @Test
    void testGetFilterFieldProcessor_Positive() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("age", Integer.class);
        String filterExpress = "name == 'John'";
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(filterExpress);
        FilterFieldProcessor filterFieldProcessor = FieldProcessorUtil.getFilterFieldProcessor(fieldMap, aviatorExpressParam, AVIATOR_FUNCTION_FACTORY);
        assertNotNull(filterFieldProcessor);
    }

    @Test
    void testGetFilterFieldProcessor_Negative() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("age", Integer.class);
        String filterExpress = "invalid filter expression";
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(filterExpress);
        assertThrows(RuntimeException.class, () -> getFilterFieldProcessor(fieldMap, aviatorExpressParam));
    }

    @Test
    void testGetTimeFieldProcessor() {
        ModelTimeColumn modelTimeColumn = new ModelTimeColumn("time", "HH:mm:ss");
        TimeFieldProcessor timeFieldProcessor = FieldProcessorUtil.getTimeFieldProcessor(modelTimeColumn);
        assertNotNull(timeFieldProcessor);
        assertEquals("time", timeFieldProcessor.timeColumnName());
        assertEquals("HH:mm:ss", timeFieldProcessor.timeFormat());
    }

    @Test
    void testGetDimensionSetProcessor_Positive() {
        String key = "key";
        String metricName = "metricName";
        List<ModelDimensionColumn> modelDimensionColumnList = new ArrayList<>();
        DimensionSetProcessor dimensionSetProcessor = FieldProcessorUtil.getDimensionSetProcessor(key, metricName, modelDimensionColumnList);
        assertNotNull(dimensionSetProcessor);
        assertEquals(key, dimensionSetProcessor.getKey());
        assertEquals(metricName, dimensionSetProcessor.getMetricName());
        assertEquals(modelDimensionColumnList, dimensionSetProcessor.getModelDimensionColumnList());
    }

    @Test
    void getMetricFieldProcessor_positiveTestCase() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("field1", Integer.class);
        fieldMap.put("field2", Integer.class);
        String metricExpress = "field1 + field2";
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(metricExpress);
        MetricFieldProcessor<Integer> metricFieldProcessor = FieldProcessorUtil.getMetricFieldProcessor(fieldMap, aviatorExpressParam, AVIATOR_FUNCTION_FACTORY);
        assertNotNull(metricFieldProcessor);
        assertEquals(fieldMap, metricFieldProcessor.getFieldMap());
        assertEquals(aviatorExpressParam, metricFieldProcessor.getAviatorExpressParam());
        Expression expression = AviatorEvaluator.compile(metricExpress, true);
        assertEquals(expression, metricFieldProcessor.getMetricExpression());
    }

    @Test
    void testGetMetricListFieldProcessor() {
        // Positive Test Case
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("metric1", Integer.class);
        fieldMap.put("metric2", Integer.class);
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("metric1");
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("metric2");
        List<AviatorExpressParam> metricExpressList = Arrays.asList(aviatorExpressParam, aviatorExpressParam1);
        MetricListFieldProcessor metricListFieldProcessor = FieldProcessorUtil.getMetricListFieldProcessor(fieldMap, metricExpressList, AVIATOR_FUNCTION_FACTORY);
        assertNotNull(metricListFieldProcessor);
        assertEquals(fieldMap, metricListFieldProcessor.getFieldMap());
        assertEquals(metricExpressList, metricListFieldProcessor.getMetricExpressParamList());
        Expression expression1 = AviatorEvaluator.compile("metric1", true);
        Expression expression2 = AviatorEvaluator.compile("metric2", true);

        assertEquals(expression1, metricListFieldProcessor.getMetricFieldProcessorList().get(0).getMetricExpression());
        assertEquals(expression2, metricListFieldProcessor.getMetricFieldProcessorList().get(1).getMetricExpression());
    }

    @Test
    void testGetDistinctFieldFieldProcessor_Positive() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("field1", String.class);
        fieldMap.put("field2", Integer.class);
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("field1");
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("field2");
        List<AviatorExpressParam> distinctFieldList = Arrays.asList(aviatorExpressParam, aviatorExpressParam1);
        MultiFieldDataFieldProcessor processor = FieldProcessorUtil.getMultiFieldDataFieldProcessor(fieldMap, distinctFieldList, AVIATOR_FUNCTION_FACTORY);
        assertNotNull(processor);
        assertEquals(fieldMap, processor.getFieldMap());
        assertEquals(distinctFieldList, processor.getAviatorExpressParamList());
        Expression expression1 = AviatorEvaluator.compile("field1", true);
        assertEquals(expression1, processor.getMetricFieldProcessorList().get(0).getMetricExpression());
        Expression expression2 = AviatorEvaluator.compile("field2", true);
        assertEquals(expression2, processor.getMetricFieldProcessorList().get(1).getMetricExpression());
    }

    @Test
    void testGetMixFieldProcessor() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("id", Integer.class);
        fieldMap.put("amount", String.class);

        MixUdafParam mixUdafParam = new MixUdafParam();
        mixUdafParam.setAggregateType("BASEMIX");
        List<MixUdafParamItem> mixUdafParamItemList = new ArrayList<>();
        mixUdafParam.setMixUdafParamItemList(mixUdafParamItemList);
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SUM");
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setMetricExpressParam(aviatorExpressParam);
        MixUdafParamItem mixUdafParamItem = new MixUdafParamItem();
        mixUdafParamItem.setName("SUM");
        mixUdafParamItem.setBaseUdafParam(baseUdafParam);
        mixUdafParamItem.setSort(0);
        mixUdafParamItemList.add(mixUdafParamItem);

        MixFieldProcessor<Object> mixFieldProcessor = FieldProcessorUtil.getMixFieldProcessor(fieldMap, mixUdafParam, AVIATOR_FUNCTION_FACTORY, AGGREGATE_FUNCTION_FACTORY);

        assertEquals(fieldMap, mixFieldProcessor.getFieldMap());
        assertEquals(mixUdafParam, mixFieldProcessor.getMixUdafParam());
        assertEquals(AGGREGATE_FUNCTION_FACTORY, mixFieldProcessor.getAggregateFunctionFactory());
        Map<String, FieldProcessor<Map<String, Object>, Object>> multiBaseAggProcessorMap = mixFieldProcessor.getMultiBaseAggProcessorMap();
        assertEquals(1, multiBaseAggProcessorMap.size());
        assertEquals(getBaseAggregateFieldProcessor(fieldMap, baseUdafParam), multiBaseAggProcessorMap.get("SUM"));
    }

    @Test
    void testGetMapFieldProcessor_Positive() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("amount", Integer.class);

        AggregateFunctionFactory factory = AGGREGATE_FUNCTION_FACTORY;

        MapUdafParam mapUdafParam = new MapUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        mapUdafParam.setDistinctFieldParamList(List.of(aviatorExpressParam));

        BaseUdafParam valueAggParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        valueAggParam.setMetricExpressParam(aviatorExpressParam1);
        valueAggParam.setAggregateType("SUM");
        mapUdafParam.setValueAggParam(valueAggParam);

        MapFieldProcessor<Pair<MultiFieldData, Integer>> mapFieldProcessor = FieldProcessorUtil.getMapFieldProcessor(fieldMap, mapUdafParam, AVIATOR_FUNCTION_FACTORY, AGGREGATE_FUNCTION_FACTORY);

        assertNotNull(mapFieldProcessor);
        assertEquals(fieldMap, mapFieldProcessor.getFieldMap());
        assertEquals(mapUdafParam, mapFieldProcessor.getMapUdafParam());
        assertEquals(factory, mapFieldProcessor.getAggregateFunctionFactory());

        assertEquals(FieldProcessorUtil.getMultiFieldDataFieldProcessor(fieldMap, mapUdafParam.getDistinctFieldParamList(), AVIATOR_FUNCTION_FACTORY), mapFieldProcessor.getKeyFieldProcessor());
        assertEquals(FieldProcessorUtil.getBaseAggregateFieldProcessor(fieldMap, valueAggParam, AVIATOR_FUNCTION_FACTORY, AGGREGATE_FUNCTION_FACTORY), mapFieldProcessor.getValueAggregateFieldProcessor());
    }

    /**
     * 数值类型
     */
    @Test
    void testNumberGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setAggregateType("SUM");
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SUM");
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setMetricExpressParam(aviatorExpressParam);
        aggregateFunctionParam.setBaseUdafParam(baseUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);

        AggregateFieldProcessor<Integer, Double, Double> aggregateFieldProcessor = getAggregateFieldProcessor(fieldMap, aggregateFunctionParam);

        FieldProcessor<Map<String, Object>, Integer> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);
        assertEquals(baseFieldProcessor, aggregateFieldProcessor.fieldProcessor());
        assertEquals(SumAggregateFunction.class, aggregateFieldProcessor.aggregateFunction().getClass());
    }

    /**
     * 对象类型
     */
    @Test
    void testObjectGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setAggregateType("FIRSTOBJECT");
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("FIRSTOBJECT");
        aggregateFunctionParam.setBaseUdafParam(baseUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);

        AggregateFieldProcessor<Map<String, Object>, MutableObj<Map<String, Object>>, Map<String, Object>> aggregateFieldProcessor = getAggregateFieldProcessor(fieldMap, aggregateFunctionParam);

        FieldProcessor<Map<String, Object>, Map<String, Object>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);
        assertEquals(baseFieldProcessor, aggregateFieldProcessor.fieldProcessor());
        assertEquals(FirstObjectAggregateFunction.class, aggregateFieldProcessor.aggregateFunction().getClass());
    }

    /**
     * 集合类型
     */
    @Test
    void testCollectionGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setAggregateType("LISTOBJECT");
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("LISTOBJECT");
        aggregateFunctionParam.setBaseUdafParam(baseUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);

        AggregateFieldProcessor<Map<String, Object>, List<Map<String, Object>>, List<Map<String, Object>>> aggregateFieldProcessor = getAggregateFieldProcessor(fieldMap, aggregateFunctionParam);

        FieldProcessor<Map<String, Object>, Map<String, Object>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);
        assertEquals(baseFieldProcessor, aggregateFieldProcessor.fieldProcessor());
        assertEquals(new ListObjectAggregateFunction(), aggregateFieldProcessor.aggregateFunction());
    }

    /**
     * 映射类型
     */
    @Test
    void testMapGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setAggregateType("BASEMAP");

        MapUdafParam mapUdafParam = new MapUdafParam();
        mapUdafParam.setAggregateType("BASEMAP");
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        mapUdafParam.setDistinctFieldParamList(ListUtil.of(aviatorExpressParam));

        BaseUdafParam valueAggParam = new BaseUdafParam();
        valueAggParam.setAggregateType("SUM");
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        valueAggParam.setMetricExpressParam(aviatorExpressParam1);

        mapUdafParam.setValueAggParam(valueAggParam);
        aggregateFunctionParam.setMapUdafParam(mapUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("amount", Integer.class);

        AggregateFieldProcessor<Pair<MultiFieldData, Integer>, Map<MultiFieldData, Double>, Map<List<Object>, Double>> aggregateFieldProcessor = getAggregateFieldProcessor(fieldMap, aggregateFunctionParam);

        FieldProcessor<Map<String, Object>, AbstractMap.SimpleImmutableEntry<MultiFieldData, Integer>> baseFieldProcessor = getMapFieldProcessor(fieldMap, mapUdafParam);
        assertEquals(baseFieldProcessor, aggregateFieldProcessor.fieldProcessor());

        BaseMapAggregateFunction<Integer, BigDecimal, BigDecimal> baseMapAggregateFunction = new BaseMapAggregateFunction<>();
        baseMapAggregateFunction.setValueAggregateFunction(new SumAggregateFunction<>());
        assertEquals(baseMapAggregateFunction, aggregateFieldProcessor.aggregateFunction());
    }

    /**
     * 混合类型
     */
    @Test
    void testMixGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setAggregateType("BASEMIX");

        MixUdafParam mixUdafParam = new MixUdafParam();
        mixUdafParam.setAggregateType("BASEMIX");
        List<MixUdafParamItem> mixUdafParamItemList = new ArrayList<>();
        mixUdafParam.setMixUdafParamItemList(mixUdafParamItemList);
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SUM");
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setMetricExpressParam(aviatorExpressParam);
        MixUdafParamItem mixUdafParamItem = new MixUdafParamItem();
        mixUdafParamItem.setName("amount");
        mixUdafParamItem.setBaseUdafParam(baseUdafParam);
        mixUdafParamItem.setSort(0);
        mixUdafParamItemList.add(mixUdafParamItem);

        mixUdafParam.setMetricExpressParam(aviatorExpressParam);
        aggregateFunctionParam.setMixUdafParam(mixUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);

        AggregateFieldProcessor<Map<String, Object>, Map<String, Object>, Double> aggregateFieldProcessor = getAggregateFieldProcessor(fieldMap, aggregateFunctionParam);

        MixFieldProcessor<Map<String, Object>> mixFieldProcessor = getMixFieldProcessor(fieldMap, mixUdafParam);
        assertEquals(mixFieldProcessor, aggregateFieldProcessor.fieldProcessor());

        BaseMixAggregateFunction<Double> baseMixAggregateFunction = new BaseMixAggregateFunction<>();
        Map<String, AggregateFunction> mixAggregateFunctionMap = new HashMap<>();
        mixAggregateFunctionMap.put("amount", new SumAggregateFunction<Integer>());
        baseMixAggregateFunction.setMixAggregateFunctionMap(mixAggregateFunctionMap);
        baseMixAggregateFunction.setExpression(AviatorEvaluator.compile("amount", true));
        assertEquals(baseMixAggregateFunction, aggregateFieldProcessor.aggregateFunction());
    }

    @Test
    void testInvalidGetAggregateFieldProcessor() {
        AggregateFunctionParam aggregateFunctionParam = new AggregateFunctionParam();
        aggregateFunctionParam.setAggregateType("invalid");

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);

        AggregateFunctionFactory aggregateFunctionFactory = Mockito.mock(AggregateFunctionFactory.class);
        AggregateFunction<Object, Object, Object> aggregateFunction = Mockito.mock(AggregateFunction.class);
        Mockito.when(aggregateFunctionFactory.getAggregateFunction("invalid")).thenReturn(aggregateFunction);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getAggregateFieldProcessor(fieldMap, aggregateFunctionParam, aggregateFunctionFactory));
        assertEquals("暂不支持聚合类型: " + aggregateFunction.getClass().getName(), runtimeException.getMessage());
    }

    @Test
    void getBaseFieldProcessor_Numerical_Test() {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("test1");
        baseUdafParam.setMetricExpressParam(aviatorExpressParam);
        baseUdafParam.setAggregateType("SUM");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("test1", Double.class);
        FieldProcessor<Map<String, Object>, Double> fieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);
        assertInstanceOf(NumberFieldProcessor.class, fieldProcessor);
        NumberFieldProcessor<Double> numberFieldProcessor = (NumberFieldProcessor<Double>) fieldProcessor;
        assertEquals(baseUdafParam, numberFieldProcessor.getUdafParam());
        assertEquals(fieldMap, numberFieldProcessor.getFieldMap());
        assertEquals(SumAggregateFunction.class.getAnnotation(Numerical.class), numberFieldProcessor.getNumerical());
        assertEquals(AviatorEvaluator.compile("test1", true), numberFieldProcessor.getMetricFieldProcessor().getMetricExpression());
        assertNull(numberFieldProcessor.getMultiFieldDataFieldProcessor());
    }

    @Test
    void getBaseFieldProcessor_Objective_Test() {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("FIRSTFIELD");
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("field1");
        baseUdafParam.setMetricExpressParam(aviatorExpressParam);
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("field1", String.class);
        fieldMap.put("field2", Integer.class);
        FieldProcessor<Map<String, Object>, String> fieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);
        assertInstanceOf(ObjectFieldProcessor.class, fieldProcessor);
    }

    @Test
    void getBaseFieldProcessor_Collective_Test() {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("LISTOBJECT");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        FieldProcessor<Map<String, Object>, Map<String, Object>> fieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);
        assertInstanceOf(CollectionFieldProcessor.class, fieldProcessor);
    }

    @Test
    void getBaseFieldProcessor_Invalid_Test() {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("BASEMIX");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getBaseAggregateFieldProcessor(fieldMap, baseUdafParam));
        assertEquals("不支持的聚合类型: BASEMIX", runtimeException.getMessage());
    }

}