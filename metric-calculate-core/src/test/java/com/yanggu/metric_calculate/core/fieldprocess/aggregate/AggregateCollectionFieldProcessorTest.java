//package com.yanggu.metric_calculate.core.fieldprocess.aggregate;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONObject;
//import com.yanggu.metric_calculate.core.annotation.MergeType;
//import com.yanggu.metric_calculate.core.fieldprocess.MetricFieldProcessor;
//import com.yanggu.metric_calculate.core.unit.MergedUnit;
//import com.yanggu.metric_calculate.core.unit.UnitFactory;
//import com.yanggu.metric_calculate.core.unit.collection.ListFieldUnit;
//import com.yanggu.metric_calculate.core.unit.collection.ListObjectUnit;
//import com.yanggu.metric_calculate.core.unit.collection.SortedListObjectUnit;
//import com.yanggu.metric_calculate.core.unit.collection.UniqueListFieldUnit;
//import com.yanggu.metric_calculate.core.value.Value;
//import com.yanggu.metric_calculate.core.value.ValueMapper;
//import org.junit.Test;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * AggregateCollectionFieldProcessor单元测试类
// * <p>聚合对集合型字段处理器单元测试类</p>
// */
//public class AggregateCollectionFieldProcessorTest {
//
//    @Test
//    public void init() {
//    }
//
//    /**
//     * 测试ListFieldUnit, 集合型, 没有比较字段以及保留指定字段
//     *
//     * @throws Exception
//     */
//    @Test
//    public void process1() throws Exception {
//
//        //构造对象型字段处理器
//        Map<String, Class<?>> fieldMap = new HashMap<>();
//        fieldMap.put("amount", Double.class);
//        fieldMap.put("name", String.class);
//
//        AggregateCollectionFieldProcessor<?> collectionFieldProcessor =
//                getCollectionFieldProcessor(ListFieldUnit.class, null, "name", fieldMap);
//
//        //构造原始数据
//        JSONObject input = new JSONObject();
//        input.set("amount", 100);
//        input.set("name", "张三");
//
//        MergedUnit process = collectionFieldProcessor.process(input);
//        assertEquals(Collections.singletonList("张三"), ValueMapper.value(((Value<?>) process)));
//
//        input.set("amount", 200);
//        input.set("name", "张三2");
//        process.merge(collectionFieldProcessor.process(input));
//        assertEquals(Arrays.asList("张三", "张三2"), ValueMapper.value((Value<?>) process));
//
//        input.set("amount", 100);
//        input.set("name", "张三3");
//        process.merge(collectionFieldProcessor.process(input));
//        assertEquals(Arrays.asList("张三", "张三2", "张三3"), ValueMapper.value((Value<?>) process));
//    }
//
//    /**
//     * 测试ListObject, 集合型, 没有比较字段以及保留原始对象数据
//     *
//     * @throws Exception
//     */
//    @Test
//    public void process2() throws Exception {
//
//        //构造对象型字段处理器
//        Map<String, Class<?>> fieldMap = new HashMap<>();
//        fieldMap.put("amount", Double.class);
//        fieldMap.put("name", String.class);
//
//        AggregateCollectionFieldProcessor<?> collectionFieldProcessor =
//                getCollectionFieldProcessor(ListObjectUnit.class, null, null, fieldMap);
//
//        //构造原始数据
//        JSONObject input = new JSONObject();
//        input.set("amount", 100);
//        input.set("name", "张三");
//
//        MergedUnit process = collectionFieldProcessor.process(input);
//        assertEquals(Collections.singletonList(input), ValueMapper.value(((Value<?>) process)));
//
//        JSONObject input2 = new JSONObject();
//        input2.set("amount", 200);
//        input2.set("name", "张三2");
//        process.merge(collectionFieldProcessor.process(input2));
//        assertEquals(Arrays.asList(input, input2), ValueMapper.value((Value<?>) process));
//
//        JSONObject input3 = new JSONObject();
//        input3.set("amount", 50);
//        input3.set("name", "张三3");
//        process.merge(collectionFieldProcessor.process(input3));
//        assertEquals(Arrays.asList(input, input2, input3), ValueMapper.value((Value<?>) process));
//    }
//
//    /**
//     * 测试SortedListObject, 集合型, 需要比较字段以及保留原始对象数据
//     *
//     * @throws Exception
//     */
//    @Test
//    public void process4() throws Exception {
//
//        //构造对象型字段处理器
//        String compareField = "amount";
//        Map<String, Class<?>> fieldMap = new HashMap<>();
//        fieldMap.put(compareField, Double.class);
//        fieldMap.put("name", String.class);
//
//        AggregateCollectionFieldProcessor<?> collectionFieldProcessor =
//                getCollectionFieldProcessor(SortedListObjectUnit.class, compareField, null, fieldMap);
//
//        //构造原始数据
//        JSONObject input = new JSONObject();
//        input.set(compareField, 100);
//        input.set("name", "张三");
//
//        MergedUnit process = collectionFieldProcessor.process(input);
//        assertEquals(Collections.singletonList(input), ValueMapper.value(((Value<?>) process)));
//
//        JSONObject input2 = new JSONObject();
//        input2.set(compareField, 200);
//        input2.set("name", "张三2");
//        process.merge(collectionFieldProcessor.process(input2));
//        assertEquals(Arrays.asList(input2, input), ValueMapper.value((Value<?>) process));
//
//        JSONObject input3 = new JSONObject();
//        input3.set(compareField, 50);
//        input3.set("name", "张三3");
//        process.merge(collectionFieldProcessor.process(input3));
//        assertEquals(Arrays.asList(input2, input, input3), ValueMapper.value((Value<?>) process));
//    }
//
//    /**
//     * 测试UniqueListField, 集合型, 有比较字段以及保留指定字段
//     *
//     * @throws Exception
//     */
//    @Test
//    public void process6() throws Exception {
//
//        //构造对象型字段处理器
//        Map<String, Class<?>> fieldMap = new HashMap<>();
//        fieldMap.put("amount", Double.class);
//        fieldMap.put("name", String.class);
//
//        AggregateCollectionFieldProcessor<?> collectionFieldProcessor =
//                getCollectionFieldProcessor(UniqueListFieldUnit.class, "amount", "name", fieldMap);
//
//        //构造原始数据
//        JSONObject input = new JSONObject();
//        input.set("amount", 100);
//        input.set("name", "张三");
//
//        MergedUnit process = collectionFieldProcessor.process(input);
//        assertEquals(Collections.singletonList("张三"), ValueMapper.value(((Value<?>) process)));
//
//        input.set("amount", 200);
//        input.set("name", "张三2");
//        process.merge(collectionFieldProcessor.process(input));
//        assertEquals(Arrays.asList("张三", "张三2"), ValueMapper.value((Value<?>) process));
//
//        input.set("amount", 100);
//        input.set("name", "张三3");
//        process.merge(collectionFieldProcessor.process(input));
//        assertEquals(Arrays.asList("张三", "张三2"), ValueMapper.value((Value<?>) process));
//    }
//
//    private AggregateCollectionFieldProcessor<?> getCollectionFieldProcessor(Class<?> clazz,
//                                                                             String compareField,
//                                                                             String retainField,
//                                                                             Map<String, Class<?>> fieldMap) throws Exception {
//        String aggregaeType = clazz.getAnnotation(MergeType.class).value();
//
//        AggregateCollectionFieldProcessor<?> aggregateCollectionFieldProcessor = new AggregateCollectionFieldProcessor<>();
//        aggregateCollectionFieldProcessor.setAggregateType(aggregaeType);
//        aggregateCollectionFieldProcessor.setMetricExpress(compareField);
//
//        UnitFactory unitFactory = new UnitFactory();
//        unitFactory.init();
//        aggregateCollectionFieldProcessor.setUnitFactory(unitFactory);
//
//        Class<? extends MergedUnit<?>> mergeableClass = unitFactory.getMergeableClass(aggregaeType);
//        aggregateCollectionFieldProcessor.setMergeUnitClazz(mergeableClass);
//
//        aggregateCollectionFieldProcessor.setFieldMap(fieldMap);
//
//        if (StrUtil.isNotBlank(retainField)) {
//            MetricFieldProcessor<Object> retainFieldProcessor = new MetricFieldProcessor<>();
//            retainFieldProcessor.setMetricExpress(retainField);
//            retainFieldProcessor.setFieldMap(fieldMap);
//            retainFieldProcessor.init();
//            aggregateCollectionFieldProcessor.setRetainFieldValueFieldProcessor(retainFieldProcessor);
//        }
//
//        //执行初始化操作
//        aggregateCollectionFieldProcessor.init();
//        return aggregateCollectionFieldProcessor;
//    }
//
//}