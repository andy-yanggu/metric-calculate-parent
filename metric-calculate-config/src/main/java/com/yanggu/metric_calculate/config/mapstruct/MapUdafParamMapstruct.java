package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.MapUdafParamDTO;
import com.yanggu.metric_calculate.config.pojo.entity.MapUdafParamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("MapUdafParamMapstruct")
@Mapper(uses = {AviatorExpressParamMapstruct.class, BaseUdafParamMapstruct.class}, componentModel = SPRING)
public interface MapUdafParamMapstruct extends BaseMapstruct<MapUdafParamDTO, MapUdafParamEntity> {

    /**
     * 转换成core中的MapUdafParam
     *
     * @param mapUdafParam
     * @return
     */
    @Named("toCoreMapUdafParam")
    @Mapping(source = "aggregateFunction.name", target = "aggregateType")
    @Mapping(source = "distinctFieldParamList", target = "distinctFieldParamList", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "valueAggParam", target = "valueAggParam", qualifiedByName = {"BaseUdafParamMapstruct", "toCoreBaseUdafParam"})
    @Mapping(source = "param", target = "param")
    com.yanggu.metric_calculate.core.pojo.udaf_param.MapUdafParam toCoreMapUdafParam(MapUdafParamEntity mapUdafParam);

}
