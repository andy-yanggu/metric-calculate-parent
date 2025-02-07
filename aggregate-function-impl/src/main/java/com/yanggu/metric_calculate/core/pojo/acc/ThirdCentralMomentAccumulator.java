package com.yanggu.metric_calculate.core.pojo.acc;

import lombok.Data;

/**
 * 三阶中心距累加器
 */
@Data
public class ThirdCentralMomentAccumulator {

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

    public ThirdCentralMomentAccumulator() {
        this.count = 0L;
        this.sum = 0.0D;
        this.sumOfSquares = 0.0D;
        this.sumOfCubes = 0.0D;
    }

    public void addValue(Double value) {
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

    public Double calculateThirdCentralMoment() {
        if (count > 0L) {
            Double mean = sum / count;
            return (sumOfCubes / count) - (3 * mean * sumOfSquares / count) + (2 * mean * mean * mean);
        } else {
            //若没有元素，则三阶中心矩为0
            return 0.0D;
        }
    }

}