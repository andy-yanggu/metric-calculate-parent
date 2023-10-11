package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.CovarianceAccumulator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 协方差单元测试类
 */
@ExtendWith(MockitoExtension.class)
class CovarianceAggregateFunctionTest {

    @Mock
    private CovarianceAccumulator mockAccumulator;

    @Spy
    private CovarianceAggregateFunction mockAggregateFunction = new CovarianceAggregateFunction();

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(CovarianceAggregateFunction.class, "COV");
    }

    @Test
    void testNumerical() {
        AggregateFunctionTestBase.testNumerical(CovarianceAggregateFunction.class, true);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(CovarianceAggregateFunction.class);
    }

    @Test
    void testCreateAccumulator1() {
        CovarianceAggregateFunction covarianceFunction = new CovarianceAggregateFunction();
        CovarianceAccumulator accumulator = covarianceFunction.createAccumulator();
        //确保累加器不为null
        assertNotNull(accumulator);
    }

    @Test
    void testCreateAccumulator2() {
        when(mockAggregateFunction.createAccumulator()).thenReturn(mockAccumulator);
        CovarianceAccumulator accumulator = mockAggregateFunction.createAccumulator();
        assertEquals(mockAccumulator, accumulator);
    }

    @Test
    void testAdd1() {
        CovarianceAggregateFunction covarianceFunction = new CovarianceAggregateFunction();
        CovarianceAccumulator accumulator = covarianceFunction.createAccumulator();
        covarianceFunction.add(List.of(2.0D, 3.0D), accumulator);
        //验证累加器中成员变量的正确性
        assertEquals(1L, accumulator.getCount());
        assertEquals(2.0D, accumulator.getSumX());
        assertEquals(3.0D, accumulator.getSumY());
        assertEquals(2.0D * 3.0D, accumulator.getSumXY());

        covarianceFunction.add(List.of(4.0D, 5.0D), accumulator);
        assertEquals(2L, accumulator.getCount());
        assertEquals(6.0D, accumulator.getSumX());
        assertEquals(8.0D, accumulator.getSumY());
        assertEquals(2.0D * 3.0D + 4.0D * 5.0D, accumulator.getSumXY());
    }

    @Test
    void testAdd2() {
        List<Double> list = List.of(2.0D, 3.0D);
        ArgumentCaptor<Double> argumentCaptor = ArgumentCaptor.forClass(Double.class);
        CovarianceAccumulator accumulator = mockAggregateFunction.add(list, mockAccumulator);
        assertEquals(mockAccumulator, accumulator);
        verify(mockAccumulator).addValue(argumentCaptor.capture(), argumentCaptor.capture());
        assertEquals(list, argumentCaptor.getAllValues());
    }

    @Test
    void testGetResult1() {
        CovarianceAggregateFunction covarianceFunction = new CovarianceAggregateFunction();
        CovarianceAccumulator accumulator = covarianceFunction.createAccumulator();

        covarianceFunction.add(List.of(2.0D, 3.0D), accumulator);
        covarianceFunction.add(List.of(4.0D, 5.0D), accumulator);
        covarianceFunction.add(List.of(6.0D, 7.0D), accumulator);

        Double result = covarianceFunction.getResult(accumulator);

        // 在这里添加对getResult方法的断言
        assertEquals(2.67D, result, 0.01D);
    }

    @Test
    void testGetResult2() {
        when(mockAccumulator.calculateCovariance()).thenReturn(100.0D);
        Double result = mockAggregateFunction.getResult(mockAccumulator);
        assertEquals(100.0D, result);
    }

    @Test
    void testMerge() {
        CovarianceAggregateFunction covarianceFunction = new CovarianceAggregateFunction();
        CovarianceAccumulator accumulator1 = covarianceFunction.createAccumulator();
        CovarianceAccumulator accumulator2 = covarianceFunction.createAccumulator();

        accumulator1.addValue(2.0D, 3.0D);
        accumulator2.addValue(4.0D, 5.0D);

        CovarianceAccumulator mergedAccumulator = covarianceFunction.merge(accumulator1, accumulator2);

        assertEquals(2, mergedAccumulator.getCount());
        assertEquals(2.0D + 4.0D, mergedAccumulator.getSumX());
        assertEquals(3.0D + 5.0D, mergedAccumulator.getSumY());
        assertEquals(2.0D * 3.0D + 4.0D * 5.0D, mergedAccumulator.getSumXY());
    }

}