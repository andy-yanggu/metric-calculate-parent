package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * 去重计数单元测试类
 */
@ExtendWith(MockitoExtension.class)
class DistinctCountAggregateFunctionTest {

    @Mock
    private HashSet<MultiFieldData> mock;

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(DistinctCountAggregateFunction.class, "DISTINCTCOUNT");
    }

    @Test
    void testCollective() {
        AggregateFunctionTestBase.testCollective(DistinctCountAggregateFunction.class, 1, 0);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(DistinctCountAggregateFunction.class);
    }

    @Test
    void testGetResult() {
        DistinctCountAggregateFunction distinctCountAggregateFunction = new DistinctCountAggregateFunction();
        when(mock.size()).thenReturn(1);
        Integer count = distinctCountAggregateFunction.getResult(mock);
        assertEquals(1, count);
    }

}