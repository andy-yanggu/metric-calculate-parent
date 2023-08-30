package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamItem;

/**
 * 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表 服务层。
 */
public interface MixUdafParamItemService extends IService<MixUdafParamItem> {

    void saveData(MixUdafParamItem mixUdafParamItem) throws Exception;

    void deleteData(MixUdafParamItem mixUdafParamItem);

}