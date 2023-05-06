package com.yanggu.metric_calculate.core2.aggregate_function.numeric;
 import org.junit.Test;
 import static org.junit.Assert.assertEquals;
 public class MinAggregateFunctionTest {
     @Test
    public void testCreateAccumulator() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        assertEquals(Double.MAX_VALUE, accumulator, 0.01D);
    }
     @Test
    public void testAddPositive() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        Double result = minAggregateFunction.add(1, accumulator);
        assertEquals(1.0D, result, 0.01D);
    }
     @Test
    public void testAddNegative() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        Double result = minAggregateFunction.add(-1, accumulator);
        assertEquals(-1.0D, result, 0.01D);
    }
     @Test
    public void testGetResultPositive() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.createAccumulator();
        Double result = minAggregateFunction.getResult(accumulator);
        assertEquals(Double.MAX_VALUE, result, 0.01D);
    }
     @Test
    public void testGetResultNegative() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double accumulator = minAggregateFunction.add(-1, minAggregateFunction.createAccumulator());
        Double result = minAggregateFunction.getResult(accumulator);
        assertEquals(-1.0D, result, 0.01D);
    }
     @Test
    public void testMergePositive() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double thisAccumulator = minAggregateFunction.add(1, minAggregateFunction.createAccumulator());
        Double thatAccumulator = minAggregateFunction.add(2, minAggregateFunction.createAccumulator());
        Double result = minAggregateFunction.merge(thisAccumulator, thatAccumulator);
        assertEquals(1.0D, result, 0.01D);
    }
     @Test
    public void testMergeNegative() {
        MinAggregateFunction<Integer> minAggregateFunction = new MinAggregateFunction<>();
        Double thisAccumulator = minAggregateFunction.add(-1, minAggregateFunction.createAccumulator());
        Double thatAccumulator = minAggregateFunction.add(-2, minAggregateFunction.createAccumulator());
        Double result = minAggregateFunction.merge(thisAccumulator, thatAccumulator);
        assertEquals(-2.0D, result, 0.01D);
    }
}