package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionInstanceDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("AviatorFunctionInstanceMapstruct")
@Mapper(componentModel = SPRING)
public interface AviatorFunctionInstanceMapstruct extends BaseMapstruct<AviatorFunctionInstanceDto, AviatorFunctionInstance> {

    @Named("toCoreInstance")
    @Mapping(source = "aviatorFunction.name", target = "name")
    @Mapping(source = "param", target = "param")
    com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorFunctionInstance toCoreInstance(AviatorFunctionInstance instance);

}
