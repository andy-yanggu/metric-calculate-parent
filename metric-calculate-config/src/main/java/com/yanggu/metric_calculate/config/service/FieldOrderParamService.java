package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.FieldOrderParam;

/**
 * 字段排序配置类 服务层。
 */
public interface FieldOrderParamService extends IService<FieldOrderParam> {

    void saveData(FieldOrderParam fieldOrderParam) throws Exception;

    void deleteData(FieldOrderParam fieldOrderParam);

}