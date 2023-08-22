package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.AviatorExpressParamDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(uses = {AviatorFunctionInstanceMapstruct.class}, componentModel = SPRING)
public interface AviatorExpressParamMapstruct extends BaseMapstruct<AviatorExpressParamDto, AviatorExpressParam> {

    @Mapping(source = "express", target = "express")
    @Mapping(source = "aviatorFunctionInstanceList", target = "aviatorFunctionInstanceList", qualifiedByName = {"AviatorFunctionInstanceMapstruct", "toCoreInstance"})
    com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam toCoreData(AviatorExpressParam param);

}
