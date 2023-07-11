package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.TimeColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.TimeColumn;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface TimeColumnMapstruct extends BaseMapstruct<TimeColumnDto, TimeColumn> {
}
