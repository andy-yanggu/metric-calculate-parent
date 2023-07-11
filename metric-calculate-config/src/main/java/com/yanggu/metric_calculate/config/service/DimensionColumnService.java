package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.ModelColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionColumn;

import java.util.List;

/**
 * 维度字段 服务层。
 */
public interface DimensionColumnService extends IService<DimensionColumn> {

    void saveTimeColumn(List<ModelColumnDto> modelColumnDtoList);

}