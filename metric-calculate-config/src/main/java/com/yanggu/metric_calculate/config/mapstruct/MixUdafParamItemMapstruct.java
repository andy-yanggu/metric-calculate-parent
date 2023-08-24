package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("MixUdafParamItemMapstruct")
@Mapper(uses = {BaseUdafParamMapstruct.class}, componentModel = SPRING)
public interface MixUdafParamItemMapstruct {

    @Named("toCoreMixUdafParamItem")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "sort", target = "sort")
    @Mapping(source = "baseUdafParam", target = "baseUdafParam", qualifiedByName = {"BaseUdafParamMapstruct", "toCoreBaseUdafParam"})
    com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParamItem toCoreMixUdafParamItem(MixUdafParamItem mixUdafParamItem);

}
