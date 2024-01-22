package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamItemEntity;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumnEntity;

import java.util.List;

/**
 * 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表 服务层。
 */
public interface MixUdafParamItemService extends IService<MixUdafParamItemEntity> {

    void saveData(MixUdafParamItemEntity mixUdafParamItem, List<ModelColumnEntity> modelColumnList) throws Exception;

    void deleteData(MixUdafParamItemEntity mixUdafParamItem);

}