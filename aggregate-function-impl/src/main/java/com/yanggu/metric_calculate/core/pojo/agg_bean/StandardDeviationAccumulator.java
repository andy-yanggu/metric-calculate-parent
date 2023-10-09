package com.yanggu.metric_calculate.core.pojo.agg_bean;

import lombok.Data;

/**
 * 标准差累加器
 */
@Data
public class StandardDeviationAccumulator {

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

    public StandardDeviationAccumulator() {
        this.count = 0L;
        this.sum = 0.0D;
        this.sumOfSquares = 0.0D;
    }

    public void addValue(Double value) {
        count++;
        sum += value;
        sumOfSquares += value * value;
    }

    public void merge(StandardDeviationAccumulator other) {
        count += other.count;
        sum += other.sum;
        sumOfSquares += other.sumOfSquares;
    }

    public Double calculateStandardDeviation() {
        if (count > 0L) {
            Double mean = sum / count;
            double variance = (sumOfSquares / count) - (mean * mean);
            return Math.sqrt(variance);
        } else {
            //若没有元素，则标准差为0
            return 0.0D;
        }
    }

}