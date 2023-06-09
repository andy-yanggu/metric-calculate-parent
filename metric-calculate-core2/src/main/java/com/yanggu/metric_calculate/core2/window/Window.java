package com.yanggu.metric_calculate.core2.window;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;

/**
 * 数据切分核心窗口
 * <p>定义数据如何切分</p>
 *
 * @param <OUT>
 */
public interface Window<OUT> {

    /**
     * 窗口类型
     *
     * @return
     */
    WindowTypeEnum type();

    /**
     * 放入明细数据进行累加
     *
     * @param input
     */
    void put(JSONObject input);

    /**
     * 查询操作
     * <p>无状态查询操作</p>
     * <p>查询实时数据</p>
     */
    DeriveMetricCalculateResult<OUT> query();

    /**
     * 查询操作
     * <p>无状态查询操作</p>
     * <p>查询历史数据</p>
     * @param input
     */
    DeriveMetricCalculateResult<OUT> query(JSONObject input);

    ///**
    // * 合并表操作
    // *
    // * @param thatTable
    // * @return
    // */
    //T merge(T thatTable);

    /**
     * 是否为空
     *
     * @return
     */
    boolean isEmpty();

    /**
     * 删除过期数据
     *
     * @param input
     */
    //public abstract void deleteExpireData(JSONObject input);

}
