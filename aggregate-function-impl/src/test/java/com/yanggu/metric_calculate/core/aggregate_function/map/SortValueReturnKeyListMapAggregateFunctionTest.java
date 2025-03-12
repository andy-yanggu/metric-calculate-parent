package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortValueReturnKeyListMapAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(SortValueReturnKeyListMapAggregateFunction.class, "SORTVALUERETURNKEYMAP");
    }

    @Test
    void testMapType() {
        AggregateFunctionTestBase.testMapType(SortValueReturnKeyListMapAggregateFunction.class);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(SortValueReturnKeyListMapAggregateFunction.class);
    }

    @Test
    void testGetResult() {
        SortValueReturnKeyListMapAggregateFunction<Double, BigDecimal, BigDecimal> aggregateFunction = new SortValueReturnKeyListMapAggregateFunction<>();
        aggregateFunction.setValueAggregateFunction(new SumAggregateFunction<>());
        //升序取4个
        aggregateFunction.setAsc(true);
        aggregateFunction.setLimit(4);

        Map<MultiFieldData, BigDecimal> accumulator = new HashMap<>();
        accumulator.put(create("test1"), BigDecimal.valueOf(0.0D));
        accumulator.put(create("test2"), BigDecimal.valueOf(-1.0D));
        accumulator.put(create("test3"), BigDecimal.valueOf(23.0D));
        accumulator.put(create("test4"), BigDecimal.valueOf(2.0D));
        accumulator.put(create("test5"), BigDecimal.valueOf(1.0D));

        List<List<Object>> result = aggregateFunction.getResult(accumulator);
        List<Object> list = result.stream().flatMap(Collection::stream).toList();
        assertEquals(List.of("test2", "test1", "test5", "test4"), list);
    }

    public static MultiFieldData create(String data) {
        return new MultiFieldData(List.of(data));
    }

}