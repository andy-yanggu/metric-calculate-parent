package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.Derive;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("DeriveMapstruct")
@Mapper(uses = {ModelDimensionColumnMapstruct.class, ModelTimeColumnMapstruct.class, AviatorExpressParamMapstruct.class, AggregateFunctionParamMapstruct.class, WindowParamMapstruct.class}, componentModel = SPRING)
public interface DeriveMapstruct extends BaseMapstruct<DeriveDto, Derive> {

    /**
     * 转换成core中的派生指标
     *
     * @param derive
     * @return
     */
    @Named("toDeriveMetrics")
    //维度字段
    @Mapping(source = "modelDimensionColumnList", target = "dimensionList", qualifiedByName = {"ModelDimensionColumnMapstruct", "toCoreDimension"})
    //时间字段
    @Mapping(source = "modelTimeColumn", target = "timeColumn", qualifiedByName = {"ModelTimeColumnMapstruct", "toCoreTimeColumn"})
    //前置过滤条件
    @Mapping(source = "filterExpressParam", target = "filterExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    //聚合函数参数
    @Mapping(source = "aggregateFunctionParam", target = "aggregateFunctionParam", qualifiedByName = {"AggregateFunctionParamMapstruct", "toCoreAggregateFunctionParam"})
    //窗口参数
    @Mapping(source = "windowParam", target = "windowParam", qualifiedByName = {"WindowParamMapstruct", "toCoreWindowParam"})
    //精度数据
    @Mapping(source = "roundAccuracyType", target = "roundAccuracy.type")
    @Mapping(source = "roundAccuracyLength", target = "roundAccuracy.length")
    DeriveMetrics toDeriveMetrics(Derive derive);

}
