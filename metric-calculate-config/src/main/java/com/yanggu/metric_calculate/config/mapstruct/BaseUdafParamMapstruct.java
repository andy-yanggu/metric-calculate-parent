package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.domain.dto.BaseUdafParamDTO;
import com.yanggu.metric_calculate.config.domain.entity.BaseUdafParamEntity;
import com.yanggu.metric_calculate.config.domain.vo.BaseUdafParamVO;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("BaseUdafParamMapstruct")
@Mapper(uses = {AviatorExpressParamMapstruct.class}, componentModel = SPRING)
public interface BaseUdafParamMapstruct extends BaseMapstruct<BaseUdafParamEntity, BaseUdafParamVO, BaseUdafParamDTO> {

    /**
     * 转换成core中的BaseUdafParam
     *
     * @param baseUdafParam
     * @return
     */
    @Named("toCoreBaseUdafParam")
    @Mapping(source = "aggregateFunction.name", target = "aggregateType")
    @Mapping(source = "metricExpressParam", target = "metricExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "metricExpressParamList", target = "metricExpressParamList", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "param", target = "param")
    BaseUdafParam toCoreBaseUdafParam(BaseUdafParamEntity baseUdafParam);

}
