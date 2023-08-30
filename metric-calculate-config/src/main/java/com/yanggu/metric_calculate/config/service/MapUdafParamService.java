package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.MapUdafParam;

/**
 * 映射类型udaf参数 服务层。
 */
public interface MapUdafParamService extends IService<MapUdafParam> {

    void saveData(MapUdafParam mapUdafParam) throws Exception;

    void deleteData(MapUdafParam mapUdafParam);

}