package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionColumn;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;

import java.util.List;

/**
 * 维度字段 服务层。
 */
public interface DimensionColumnService extends IService<DimensionColumn> {

    /**
     * 保存维度字段
     *
     */
    void saveDimensionColumnList(List<ModelColumn> modelColumnList);

}