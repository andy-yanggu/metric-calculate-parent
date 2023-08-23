package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.entity.FieldOrderParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("FieldOrderParamMapstruct")
@Mapper(uses = {AviatorExpressParamMapstruct.class}, componentModel = SPRING)
public interface FieldOrderParamMapstruct {

    @Named("toCoreFieldOrderParam")
    @Mapping(source = "aviatorExpressParam", target = "aviatorExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "isAsc", target = "isAsc")
    com.yanggu.metric_calculate.core.field_process.multi_field_order.FieldOrderParam toCoreFieldOrderParam(FieldOrderParam fieldOrderParam);


}
