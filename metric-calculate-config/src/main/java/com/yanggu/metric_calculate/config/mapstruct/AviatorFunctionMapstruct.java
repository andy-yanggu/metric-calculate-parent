package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionDTO;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AviatorFunctionMapstruct extends BaseMapstruct<AviatorFunctionDTO, AviatorFunctionEntity> {
}
