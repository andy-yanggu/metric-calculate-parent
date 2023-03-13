package com.yanggu.metric_calculate.core.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @version V1.0
 * @author: YangGu
 * @date: 2023/3/13 17:40
 * @description:
 */
public class AvgFunction implements AggregateFunction<Double, AvgFunction.Avg, Double>{

    @Override
    public Avg createAccumulator() {
        return new Avg();
    }

    @Override
    public Avg add(Double value, Avg accumulator) {
        accumulator.count++;
        accumulator.sum += value;
        return accumulator;
    }

    @Override
    public Double getResult(Avg accumulator) {
        return accumulator.sum / accumulator.count;
    }

    @Override
    public Avg merge(Avg a, Avg b) {
        a.sum += b.sum;
        a.count += b.count;
        return a;
    }

    public static class Avg {

        private long count;

        private double sum;

    }

    private static final Map<String, Avg> map = new HashMap<>();

    public static void main(String[] args) {
        AvgFunction avgFunction = new AvgFunction();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String next = scanner.next();
            String[] split = next.split(",");
            String id = split[0];
            double amount = Double.parseDouble(split[1]);
            Avg avg = map.computeIfAbsent(id, tempId -> avgFunction.createAccumulator());
            avgFunction.add(amount, avg);
            System.out.println("id: " + id + ", 平均金额: " + avgFunction.getResult(avg));
        }
    }

}
