package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.domain.entity.ModelColumnEntity;

import java.util.List;

/**
 * 宽表字段 服务层。
 */
public interface ModelColumnService extends IService<ModelColumnEntity> {

    void saveModelColumnList(List<ModelColumnEntity> modelColumnList) throws Exception;

}