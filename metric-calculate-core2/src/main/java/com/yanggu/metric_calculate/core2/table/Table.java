package com.yanggu.metric_calculate.core2.table;


public interface Table<IN, ACC, OUT> {

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
