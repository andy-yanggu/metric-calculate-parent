package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.ModelDimensionColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.ModelDimensionColumn;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ModelDimensionColumnMapstruct extends BaseMapstruct<ModelDimensionColumnDto, ModelDimensionColumn> {
}
