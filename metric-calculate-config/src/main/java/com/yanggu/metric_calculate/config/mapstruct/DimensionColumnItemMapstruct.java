package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.DimensionColumnItemDto;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionColumnItem;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DimensionColumnItemMapstruct extends BaseMapstruct<DimensionColumnItemDto, DimensionColumnItem> {
}
