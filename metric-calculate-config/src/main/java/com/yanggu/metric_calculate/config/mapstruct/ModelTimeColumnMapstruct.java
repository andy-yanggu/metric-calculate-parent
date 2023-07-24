package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.ModelTimeColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.ModelTimeColumn;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ModelTimeColumnMapstruct extends BaseMapstruct<ModelTimeColumnDto, ModelTimeColumn> {
}
