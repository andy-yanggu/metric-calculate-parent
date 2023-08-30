package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionParam;

/**
 * 聚合函数参数配置类 服务层。
 */
public interface AggregateFunctionParamService extends IService<AggregateFunctionParam> {

    void saveData(AggregateFunctionParam aggregateFunctionParam) throws Exception;

    void deleteData(AggregateFunctionParam aggregateFunctionParam);

}