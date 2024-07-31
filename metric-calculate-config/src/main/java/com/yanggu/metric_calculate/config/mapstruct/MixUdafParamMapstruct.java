package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.domain.dto.MixUdafParamDTO;
import com.yanggu.metric_calculate.config.domain.entity.MixUdafParamEntity;
import com.yanggu.metric_calculate.config.domain.vo.MixUdafParamVO;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("MixUdafParamMapstruct")
@Mapper(uses = {AviatorExpressParamMapstruct.class, MixUdafParamItemMapstruct.class}, componentModel = SPRING)
public interface MixUdafParamMapstruct extends BaseMapstruct<MixUdafParamEntity, MixUdafParamVO, MixUdafParamDTO> {

    /**
     * 转换成core中的MixUdafParam
     *
     * @param mixUdafParam
     * @return
     */
    @Named("toCoreMixUdafParam")
    @Mapping(source = "aggregateFunction.name", target = "aggregateType")
    @Mapping(source = "mixUdafParamItemList", target = "mixUdafParamItemList", qualifiedByName = {"MixUdafParamItemMapstruct", "toCoreMixUdafParamItem"})
    @Mapping(source = "metricExpressParam", target = "metricExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "param", target = "param")
    MixUdafParam toCoreMixUdafParam(MixUdafParamEntity mixUdafParam);

}
