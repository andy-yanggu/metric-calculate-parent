package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunction;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AviatorFunctionMapstruct extends BaseMapstruct<AviatorFunctionDto, AviatorFunction> {
}
