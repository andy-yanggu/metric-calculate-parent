package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.MapUdafParamEntity;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumnEntity;

import java.util.List;

/**
 * 映射类型udaf参数 服务层。
 */
public interface MapUdafParamService extends IService<MapUdafParamEntity> {

    void saveData(MapUdafParamEntity mapUdafParam, List<ModelColumnEntity> modelColumnList) throws Exception;

    void deleteData(MapUdafParamEntity mapUdafParam);

}