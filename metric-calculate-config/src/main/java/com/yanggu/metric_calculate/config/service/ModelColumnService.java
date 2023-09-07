package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.Model;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;

import java.util.List;

/**
 * 宽表字段 服务层。
 */
public interface ModelColumnService extends IService<ModelColumn> {

    void saveModelColumnList(List<ModelColumn> modelColumnList) throws Exception;

}