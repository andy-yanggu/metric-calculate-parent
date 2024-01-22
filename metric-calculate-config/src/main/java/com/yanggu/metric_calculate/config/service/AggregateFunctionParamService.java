package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionParamEntity;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumnEntity;

import java.util.List;

/**
 * 聚合函数参数配置类 服务层。
 */
public interface AggregateFunctionParamService extends IService<AggregateFunctionParamEntity> {

    void saveData(AggregateFunctionParamEntity aggregateFunctionParam, List<ModelColumnEntity> modelColumnList) throws Exception;

    void deleteData(AggregateFunctionParamEntity aggregateFunctionParam);

}