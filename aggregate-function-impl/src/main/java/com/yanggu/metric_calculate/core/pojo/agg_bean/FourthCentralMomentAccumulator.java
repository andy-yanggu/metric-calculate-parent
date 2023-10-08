package com.yanggu.metric_calculate.core.pojo.agg_bean;

import lombok.Data;

@Data
public class FourthCentralMomentAccumulator {

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

    /**
     * 元素四次方的和
     */
    private double sumOfQuartics;

    public FourthCentralMomentAccumulator() {
        this.count = 0L;
        this.sum = 0.0D;
        this.sumOfSquares = 0.0D;
        this.sumOfCubes = 0.0D;
        this.sumOfQuartics = 0.0D;
    }

    public void addValue(double value) {
        count++;
        sum += value;
        sumOfSquares += value * value;
        sumOfCubes += value * value * value;
        sumOfQuartics += value * value * value * value;
    }

    public void merge(FourthCentralMomentAccumulator other) {
        count += other.count;
        sum += other.sum;
        sumOfSquares += other.sumOfSquares;
        sumOfCubes += other.sumOfCubes;
        sumOfQuartics += other.sumOfQuartics;
    }

    public double calculateFourthCentralMoment() {
        if (count > 0L) {
            double mean = sum / count;
            return (sumOfQuartics / count) - (4 * mean * sumOfCubes / count) + (6 * mean * mean * sumOfSquares / count) - (3 * mean * mean * mean * mean);
        } else {
            //若没有元素，则四阶中心矩为0
            return 0.0D;
        }
    }

}