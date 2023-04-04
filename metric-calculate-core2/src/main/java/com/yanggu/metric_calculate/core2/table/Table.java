package com.yanggu.metric_calculate.core2.table;

/**
 * 数据分桶方式
 *
 * @param <IN>
 * @param <OUT>
 */
public interface Table<IN, OUT> {

    /**
     * 初始化接口
     */
    default void init() {
    }

    /**
     * 根据度量值和时间戳进行累加
     *
     * @param timestamp
     * @param in
     */
    void put(Long timestamp, IN in);

    /**
     * 查询操作
     *
     * @return
     */
    OUT query();

}
