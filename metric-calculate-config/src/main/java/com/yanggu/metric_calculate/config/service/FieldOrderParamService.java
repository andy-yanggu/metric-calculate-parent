package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.FieldOrderParam;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;

import java.util.List;

/**
 * 字段排序配置类 服务层。
 */
public interface FieldOrderParamService extends IService<FieldOrderParam> {

    void saveData(FieldOrderParam fieldOrderParam, List<ModelColumn> modelColumnList) throws Exception;

    void deleteData(FieldOrderParam fieldOrderParam);

}