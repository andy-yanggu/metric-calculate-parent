package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.domain.dto.MapUdafParamDTO;
import com.yanggu.metric_calculate.config.domain.entity.MapUdafParamEntity;
import com.yanggu.metric_calculate.config.domain.vo.MapUdafParamVO;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MapUdafParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("MapUdafParamMapstruct")
@Mapper(uses = {AviatorExpressParamMapstruct.class, BaseUdafParamMapstruct.class}, componentModel = SPRING)
public interface MapUdafParamMapstruct extends BaseMapstruct<MapUdafParamEntity, MapUdafParamVO, MapUdafParamDTO> {

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
    MapUdafParam toCoreMapUdafParam(MapUdafParamEntity mapUdafParam);

}
