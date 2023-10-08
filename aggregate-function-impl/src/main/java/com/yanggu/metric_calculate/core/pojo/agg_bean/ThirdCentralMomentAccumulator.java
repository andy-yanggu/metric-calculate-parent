package com.yanggu.metric_calculate.core.pojo.agg_bean;

import lombok.Data;

@Data
public class ThirdCentralMomentAccumulator {

    /**
     * 计数器，用于统计元素数量
     */
    private long count;

    /**
     * 元素的和
     */
    private double sum;

    /**
     * 元素平方的和
     */
    private double sumOfSquares;

    /**
     * 元素立方的和
     */
    private double sumOfCubes;

    public ThirdCentralMomentAccumulator() {
        this.count = 0L;
        this.sum = 0.0D;
        this.sumOfSquares = 0.0D;
        this.sumOfCubes = 0.0D;
    }

    public void addValue(double value) {
        count++;
        sum += value;
        sumOfSquares += value * value;
        sumOfCubes += value * value * value;
    }

    public void merge(ThirdCentralMomentAccumulator other) {
        count += other.count;
        sum += other.sum;
        sumOfSquares += other.sumOfSquares;
        sumOfCubes += other.sumOfCubes;
    }

    public double calculateThirdCentralMoment() {
        if (count > 0L) {
            double mean = sum / count;
            return (sumOfCubes / count) - (3 * mean * sumOfSquares / count) + (2 * mean * mean * mean);
        } else {
            //若没有元素，则三阶中心矩为0
            return 0.0D;
        }
    }

}