package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionInstanceDTO;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstanceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("AviatorFunctionInstanceMapstruct")
@Mapper(componentModel = SPRING)
public interface AviatorFunctionInstanceMapstruct extends BaseMapstruct<AviatorFunctionInstanceDTO, AviatorFunctionInstanceEntity> {

    /**
     * 转成成core中的AviatorFunctionInstance
     *
     * @param instance
     * @return
     */
    @Named("toCoreInstance")
    @Mapping(source = "aviatorFunction.name", target = "name")
    @Mapping(source = "param", target = "param")
    com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorFunctionInstance toCoreInstance(AviatorFunctionInstanceEntity instance);

}
