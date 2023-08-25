package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.entity.NodePattern;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("NodePatternMapstruct")
@Mapper(uses = {AviatorExpressParamMapstruct.class}, componentModel = SPRING)
public interface NodePatternMapstruct {

    @Named("toCoreNodePattern")
    @Mapping(source = "matchExpressParam", target = "matchExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    com.yanggu.metric_calculate.core.pojo.udaf_param.NodePattern toCoreNodePattern(NodePattern nodePattern);

}
