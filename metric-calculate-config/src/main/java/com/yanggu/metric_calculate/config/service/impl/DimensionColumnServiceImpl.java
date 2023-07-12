package com.yanggu.metric_calculate.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapstruct.DimensionColumnMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.ModelColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionColumn;
import com.yanggu.metric_calculate.config.mapper.DimensionColumnMapper;
import com.yanggu.metric_calculate.config.service.DimensionColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 维度字段 服务层实现。
 */
@Service
public class DimensionColumnServiceImpl extends ServiceImpl<DimensionColumnMapper, DimensionColumn> implements DimensionColumnService {

    @Autowired
    private DimensionColumnMapstruct dimensionColumnMapstruct;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveDimensionColumn(List<ModelColumnDto> modelColumnDtoList) {
        if (CollUtil.isEmpty(modelColumnDtoList)) {
            return;
        }
        List<DimensionColumn> dimensionColumnList = modelColumnDtoList.stream()
                .filter(tempModelColumnDto -> tempModelColumnDto.getDimensionColumn() != null)
                .map(tempModelColumnDto -> {
                    DimensionColumn dimensionColumn = dimensionColumnMapstruct.toEntity(tempModelColumnDto.getDimensionColumn());
                    dimensionColumn.setModelColumnId(tempModelColumnDto.getId());
                    dimensionColumn.setUserId(tempModelColumnDto.getUserId());
                    return dimensionColumn;
                })
                .collect(Collectors.toList());

        if (CollUtil.isNotEmpty(dimensionColumnList)) {
            saveBatch(dimensionColumnList);
        }
    }

}