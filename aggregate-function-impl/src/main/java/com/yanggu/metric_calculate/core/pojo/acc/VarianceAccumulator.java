package com.yanggu.metric_calculate.core.pojo.acc;

import lombok.Data;

/**
 * 方差累加器
 */
@Data
public class VarianceAccumulator {

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

    public VarianceAccumulator() {
        this.count = 0L;
        this.sum = 0.0D;
        this.sumOfSquares = 0.0D;
    }

    public void addValue(Double value) {
        count++;
        sum += value;
        sumOfSquares += value * value;
    }

    public void merge(VarianceAccumulator other) {
        count += other.count;
        sum += other.sum;
        sumOfSquares += other.sumOfSquares;
    }

    public Double calculateVariance() {
        if (count > 0L) {
            Double mean = sum / count;
            return (sumOfSquares / count) - (mean * mean);
        } else {
            //若没有元素，则方差为0
            return 0.0D;
        }
    }

}