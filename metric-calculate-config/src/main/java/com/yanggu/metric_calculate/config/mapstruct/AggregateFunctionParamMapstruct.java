package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("AggregateFunctionParamMapstruct")
@Mapper(
        uses = {
                BaseUdafParamMapstruct.class,
                MapUdafParamMapstruct.class,
                MixUdafParamMapstruct.class
        },
        componentModel = SPRING)
public interface AggregateFunctionParamMapstruct {

    @Named("toCoreAggregateFunctionParam")
    @Mapping(source = "aggregateFunction.name", target = "aggregateType")
    @Mapping(source = "baseUdafParam", target = "baseUdafParam", qualifiedByName = {"BaseUdafParamMapstruct", "toCoreBaseUdafParam"})
    @Mapping(source = "mapUdafParam", target = "mapUdafParam", qualifiedByName = {"MapUdafParamMapstruct", "toCoreMapUdafParam"})
    @Mapping(source = "mixUdafParam", target = "mixUdafParam", qualifiedByName = {"MixUdafParamMapstruct", "toCoreMixUdafParam"})
    com.yanggu.metric_calculate.core.pojo.metric.AggregateFunctionParam toCoreAggregateFunctionParam(AggregateFunctionParam aggregateFunctionParam);

}
