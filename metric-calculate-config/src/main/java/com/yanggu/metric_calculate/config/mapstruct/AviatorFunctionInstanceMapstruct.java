package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorFunctionInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("AviatorFunctionInstanceMapstruct")
@Mapper(componentModel = SPRING)
public interface AviatorFunctionInstanceMapstruct {

    @Named("toCoreInstance")
    @Mapping(source = "aviatorFunction.name", target = "name")
    @Mapping(source = "param", target = "param")
    AviatorFunctionInstance toCoreInstance(com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstance instance);

}
