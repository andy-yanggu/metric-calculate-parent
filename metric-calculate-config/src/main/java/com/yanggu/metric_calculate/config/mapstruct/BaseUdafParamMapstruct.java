package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.BaseUdafParamDto;
import com.yanggu.metric_calculate.config.pojo.entity.BaseUdafParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("BaseUdafParamMapstruct")
@Mapper(uses = {AviatorExpressParamMapstruct.class, FieldOrderParamMapstruct.class}, componentModel = SPRING)
public interface BaseUdafParamMapstruct extends BaseMapstruct<BaseUdafParamDto, BaseUdafParam> {

    @Named("toCoreBaseUdafParam")
    @Mapping(source = "aggregateFunction.name", target = "aggregateType")
    @Mapping(source = "metricExpressParam", target = "metricExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "metricExpressParamList", target = "metricExpressParamList", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "retainExpressParam", target = "retainExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "objectiveCompareFieldParamList", target = "objectiveCompareFieldParamList", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "distinctFieldListParamList", target = "distinctFieldListParamList", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "collectiveSortFieldList", target = "collectiveSortFieldList", qualifiedByName = {"FieldOrderParamMapstruct", "toCoreFieldOrderParam"})
    @Mapping(source = "param", target = "param")
    com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam toCoreBaseUdafParam(BaseUdafParam baseUdafParam);

}
