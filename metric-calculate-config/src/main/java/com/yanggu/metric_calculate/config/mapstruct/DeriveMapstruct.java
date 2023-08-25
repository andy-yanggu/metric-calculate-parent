package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.Derive;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("DeriveMapstruct")
@Mapper(
        uses = {
                ModelDimensionColumnMapstruct.class,
                ModelTimeColumnMapstruct.class,
                AviatorExpressParamMapstruct.class,
                AggregateFunctionParamMapstruct.class,
                WindowParamMapstruct.class
        },
        componentModel = SPRING)
public interface DeriveMapstruct extends BaseMapstruct<DeriveDto, Derive> {

    @Named("toDeriveMetrics")
    @Mapping(source = "modelDimensionColumnList", target = "dimensionList", qualifiedByName = {"ModelDimensionColumnMapstruct", "toCoreDimension"})
    @Mapping(source = "modelTimeColumn", target = "timeColumn", qualifiedByName = {"ModelTimeColumnMapstruct", "toCoreTimeColumn"})
    @Mapping(source = "filterExpressParam", target = "filterExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "aggregateFunctionParam", target = "aggregateFunctionParam", qualifiedByName = {"AggregateFunctionParamMapstruct", "toCoreAggregateFunctionParam"})
    @Mapping(source = "windowParam", target = "windowParam", qualifiedByName = {"WindowParamMapstruct", "toCoreWindowParam"})
    DeriveMetrics toDeriveMetrics(Derive derive);

}
