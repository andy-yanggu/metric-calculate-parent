package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.DimensionDto;
import com.yanggu.metric_calculate.config.pojo.entity.Dimension;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DimensionMapstruct extends BaseMapstruct<DimensionDto, Dimension> {
}
