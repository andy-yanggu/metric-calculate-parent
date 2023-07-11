package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.DimensionColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionColumn;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DimensionColumnMapstruct extends BaseMapstruct<DimensionColumnDto, DimensionColumn> {
}
