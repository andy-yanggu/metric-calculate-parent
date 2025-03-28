package com.yanggu.metric_calculate.core.window;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;

import java.util.Map;

/**
 * 数据切分核心接口
 * <p>定义数据如何切分</p>
 * <p>窗口内的数据如何查询</p>
 *
 * @param <OUT>
 */
public interface Window<OUT> {

    /**
     * 初始化接口
     */
    default void init() {
    }

    /**
     * 放入明细数据进行累加
     *
     * @param input
     */
    void put(Map<String, Object> input);

    /**
     * 查询操作
     * <p>无状态查询操作</p>
     * <p>根据窗口条件查询</p>
     * <p>窗口数据是内置的</p>
     */
    DeriveMetricCalculateResult<OUT> query();

    /**
     * 查询操作
     * <p>无状态查询操作</p>
     * <p>根据窗口条件查询</p>
     * <p>窗口数据是通过input传入的</p>
     */
    DeriveMetricCalculateResult<OUT> query(Map<String, Object> input);

    /**
     * 删除数据
     * <p>根据窗口条件删除数据</p>
     * <p>窗口条件是内部的</p>
     */
    void deleteData();

    ///**
    // * 合并操作
    // *
    // * @param thatWindow
    // * @return
    // */
    //W merge(W thatWindow);

    /**
     * 窗口是否为空
     *
     * @return
     */
    boolean isEmpty();

}
