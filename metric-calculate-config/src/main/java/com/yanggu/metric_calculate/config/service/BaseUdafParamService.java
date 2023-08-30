package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.BaseUdafParam;

/**
 * 数值型、集合型、对象型聚合函数相关参数 服务层。
 */
public interface BaseUdafParamService extends IService<BaseUdafParam> {

    void saveData(BaseUdafParam baseUdafParam) throws Exception;

    void deleteData(BaseUdafParam baseUdafParam);

}