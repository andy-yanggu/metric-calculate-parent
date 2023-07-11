package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.pojo.dto.ModelColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ModelColumnMapstruct extends BaseMapstruct<ModelColumnDto, ModelColumn> {
}
