package com.yanggu.metric_calculate.core.pojo.agg_bean;

import lombok.Data;

@Data
public class CovarianceAccumulator {

    /**
     * 计数器，用于统计元素数量
     */
    private long count;

    /**
     * X 元素的和
     */
    private double sumX;

    /**
     * Y 元素的和
     */
    private double sumY;

    /**
     * X 和 Y 元素的乘积的和
     */
    private double sumXY;

    public CovarianceAccumulator() {
        this.count = 0L;
        this.sumX = 0.0D;
        this.sumY = 0.0D;
        this.sumXY = 0.0D;
    }

    public void addValue(double x, double y) {
        count++;
        sumX += x;
        sumY += y;
        sumXY += x * y;
    }

    public void merge(CovarianceAccumulator other) {
        count += other.count;
        sumX += other.sumX;
        sumY += other.sumY;
        sumXY += other.sumXY;
    }

    public double calculateCovariance() {
        if (count > 0L) {
            double meanX = sumX / count;
            double meanY = sumY / count;
            return (sumXY / count) - (meanX * meanY);
        } else {
            //若没有元素，则协方差为0
            return 0.0D;
        }

    }

}