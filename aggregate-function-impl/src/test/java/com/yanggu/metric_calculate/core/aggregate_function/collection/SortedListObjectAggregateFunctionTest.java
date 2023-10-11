package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldOrderCompareKey;
import org.dromara.hutool.core.collection.queue.BoundedPriorityQueue;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SortedListObjectAggregateFunction有界有序对象列表单元测试类
 */
class SortedListObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(SortedListObjectAggregateFunction.class, "SORTEDLIMITLISTOBJECT");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(SortedListObjectAggregateFunction.class, 2, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(SortedListObjectAggregateFunction.class);
    }

    @Test
    void createAccumulator() {
        SortedListObjectAggregateFunction sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction();
        BoundedPriorityQueue<KeyValue<MultiFieldOrderCompareKey, JSONObject>> accumulator = sortedListObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
        assertEquals(10, FieldUtil.getFieldValue(accumulator, "capacity"));
    }

    @Test
    void testAdd1() {
        SortedListObjectAggregateFunction sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction();
        //设置为3个
        sortedListObjectAggregateFunction.setLimit(3);
        BoundedPriorityQueue<KeyValue<MultiFieldOrderCompareKey, JSONObject>> accumulator = sortedListObjectAggregateFunction.createAccumulator();

        var keyValue1 = createKeyValue(1);
        sortedListObjectAggregateFunction.add(keyValue1, accumulator);
        var integers = accumulator.toList();
        assertEquals(List.of(keyValue1), integers);

        var keyValue3 = createKeyValue(3);
        sortedListObjectAggregateFunction.add(keyValue3, accumulator);
        integers = accumulator.toList();
        assertEquals(List.of(keyValue1, keyValue3), integers);

        var keyValue2 = createKeyValue(2);
        sortedListObjectAggregateFunction.add(keyValue2, accumulator);
        integers = accumulator.toList();
        assertEquals(List.of(keyValue1, keyValue2, keyValue3), integers);

        var keyValue4 = createKeyValue(4);
        sortedListObjectAggregateFunction.add(keyValue4, accumulator);
        integers = accumulator.toList();
        assertEquals(List.of(keyValue1, keyValue2, keyValue3), integers);

        sortedListObjectAggregateFunction.add(keyValue1, accumulator);
        integers = accumulator.toList();
        assertEquals(List.of(keyValue1, keyValue1, keyValue2), integers);
    }

    @Test
    void getResult() {
        SortedListObjectAggregateFunction sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction();
        var accumulator = sortedListObjectAggregateFunction.createAccumulator();

        var keyValue1 = createKeyValue(1);
        sortedListObjectAggregateFunction.add(keyValue1, accumulator);

        var result = sortedListObjectAggregateFunction.getResult(accumulator);
        assertEquals(List.of(keyValue1.getValue()), result);
    }

    @Test
    void merge() {
        var sortedListObjectAggregateFunction = new SortedListObjectAggregateFunction();
        sortedListObjectAggregateFunction.setLimit(3);
        var test1 = new SortedListObjectAggregateFunction();

        var accumulator1 = sortedListObjectAggregateFunction.createAccumulator();
        var accumulator2 = sortedListObjectAggregateFunction.createAccumulator();

        var keyValue1 = createKeyValue(1);
        sortedListObjectAggregateFunction.add(keyValue1, accumulator1);

        sortedListObjectAggregateFunction.add(createKeyValue(3), accumulator1);

        KeyValue<MultiFieldOrderCompareKey, JSONObject> keyValue2 = createKeyValue(2);
        sortedListObjectAggregateFunction.add(keyValue2, accumulator1);
        sortedListObjectAggregateFunction.add(createKeyValue(6), accumulator1);
        sortedListObjectAggregateFunction.add(createKeyValue(5), accumulator1);

        var keyValue3 = createKeyValue(-1);
        sortedListObjectAggregateFunction.add(keyValue3, accumulator2);
        sortedListObjectAggregateFunction.add(createKeyValue(2), accumulator2);
        sortedListObjectAggregateFunction.add(createKeyValue(3), accumulator2);

        var result = sortedListObjectAggregateFunction.getResult(sortedListObjectAggregateFunction.merge(accumulator1, accumulator2));

        assertEquals(List.of(keyValue3.getValue(), keyValue1.getValue(), keyValue2.getValue()), result);
    }

    private KeyValue<MultiFieldOrderCompareKey, JSONObject> createKeyValue(int data) {
        MultiFieldOrderCompareKey multiFieldOrderCompareKey = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey.setDataList(List.of(data));
        multiFieldOrderCompareKey.setBooleanList(List.of(true));
        JSONObject jsonObject = new JSONObject();
        return new KeyValue<>(multiFieldOrderCompareKey, jsonObject);
    }

}