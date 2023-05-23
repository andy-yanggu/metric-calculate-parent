package com.yanggu.metric_calculate.core2.table;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;

public interface Table<IN, ACC, OUT> {

    /**
     * 放入明细数据进行累加
     *
     * @param input
     */
    void put(JSONObject input);

    /**
     * 查询操作
     * <p>查询实时数据</p>
     *
     * @return
     */
    DeriveMetricCalculateResult<OUT> query();

    /**
     * 查询操作
     * <p>查询历史数据</p>
     *
     * @return
     */
    DeriveMetricCalculateResult<OUT> query(JSONObject input);

    /**
     * 合并表操作
     *
     * @param that
     * @return
     */
    //Table<IN, ACC, OUT> merge(Table<IN, ACC, OUT> that);

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
