package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.MapUdafParam;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;

import java.util.List;

/**
 * 映射类型udaf参数 服务层。
 */
public interface MapUdafParamService extends IService<MapUdafParam> {

    void saveData(MapUdafParam mapUdafParam, List<ModelColumn> modelColumnList) throws Exception;

    void deleteData(MapUdafParam mapUdafParam);

}