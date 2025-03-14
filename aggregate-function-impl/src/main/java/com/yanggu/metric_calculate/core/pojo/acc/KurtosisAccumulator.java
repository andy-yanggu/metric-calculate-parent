package com.yanggu.metric_calculate.core.pojo.acc;

import lombok.Data;

/**
 * 峰度系数累加器
 */
@Data
public class KurtosisAccumulator {

    /**
     * 计数器，用于统计元素数量
     */
    private Long count;

    /**
     * 元素的和
     */
    private Double sum;

    /**
     * 元素平方的和
     */
    private Double sumOfSquares;

    /**
     * 元素立方的和
     */
    private Double sumOfCubes;

    /**
     * 元素四次方的和
     */
    private Double sumOfQuartics;

    public KurtosisAccumulator() {
        this.count = 0L;
        this.sum = 0.0D;
        this.sumOfSquares = 0.0D;
        this.sumOfCubes = 0.0D;
        this.sumOfQuartics = 0.0D;
    }

    public void addValue(Double value) {
        count++;
        sum += value;
        sumOfSquares += value * value;
        sumOfCubes += value * value * value;
        sumOfQuartics += value * value * value * value;
    }

    public void merge(KurtosisAccumulator other) {
        count += other.count;
        sum += other.sum;
        sumOfSquares += other.sumOfSquares;
        sumOfCubes += other.sumOfCubes;
        sumOfQuartics += other.sumOfQuartics;
    }

    public Double calculateKurtosis() {
        if (count > 0L) {
            Double mean = sum / count;
            Double variance = (sumOfSquares / count) - (mean * mean);
            double fourthMoment = (sumOfQuartics / count) - (4 * mean * sumOfCubes / count) + (6 * mean * mean * sumOfSquares / count) - (3 * mean * mean * mean * mean);
            return fourthMoment / (variance * variance);
        } else {
            //若没有元素，则四阶中心矩为0
            return 0.0D;
        }
    }

}