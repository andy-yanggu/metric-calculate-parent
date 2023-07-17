package com.yanggu.metric_calculate.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapstruct.DimensionColumnItemMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.DimensionColumnItemDto;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionColumnItem;
import com.yanggu.metric_calculate.config.mapper.DimensionColumnItemMapper;
import com.yanggu.metric_calculate.config.service.DimensionColumnItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 维度字段选项 服务层实现。
 */
@Service
public class DimensionColumnItemServiceImpl extends ServiceImpl<DimensionColumnItemMapper, DimensionColumnItem> implements DimensionColumnItemService {

    @Autowired
    private DimensionColumnItemMapstruct dimensionColumnItemMapstruct;

    @Autowired
    private DimensionColumnItemMapper dimensionColumnItemMapper;

    @Override
    public void save(List<DimensionColumnItemDto> dimensionColumnItemDtoList) {
        if (CollUtil.isEmpty(dimensionColumnItemDtoList)) {
            return;
        }
        List<DimensionColumnItem> entityList = dimensionColumnItemMapstruct.toEntity(dimensionColumnItemDtoList);
        saveBatch(entityList);
    }

}