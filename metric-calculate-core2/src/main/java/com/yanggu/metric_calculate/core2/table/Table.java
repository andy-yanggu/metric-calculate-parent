package com.yanggu.metric_calculate.core2.table;


public interface Table<IN, ACC, OUT> {

    /**
     * 放入度量值和时间戳进行累加
     *
     * @param timestamp
     * @param in
     */
    void put(Long timestamp, IN in);

    /**
     * 查询数据
     *
     * @param from
     * @param fromInclusive
     * @param to
     * @param toInclusive
     * @return
     */
    OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive);

}
