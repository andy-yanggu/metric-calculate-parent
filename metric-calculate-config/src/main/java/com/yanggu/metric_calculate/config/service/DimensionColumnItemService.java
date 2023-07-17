package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.DimensionColumnItemDto;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionColumnItem;

import java.util.List;

/**
 * 维度字段选项 服务层。
 */
public interface DimensionColumnItemService extends IService<DimensionColumnItem> {

    void save(List<DimensionColumnItemDto> dimensionColumnItemDtoList);

}