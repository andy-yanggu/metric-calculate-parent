package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.domain.dto.MixUdafParamItemDTO;
import com.yanggu.metric_calculate.config.domain.entity.MixUdafParamItemEntity;
import com.yanggu.metric_calculate.config.domain.vo.MixUdafParamItemVO;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParamItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("MixUdafParamItemMapstruct")
@Mapper(uses = {BaseUdafParamMapstruct.class, MapUdafParamMapstruct.class}, componentModel = SPRING)
public interface MixUdafParamItemMapstruct extends BaseMapstruct<MixUdafParamItemEntity, MixUdafParamItemVO, MixUdafParamItemDTO> {

    /**
     * 转换成core中的MixUdafParamItem
     *
     * @param mixUdafParamItem
     * @return
     */
    @Named("toCoreMixUdafParamItem")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "sort", target = "sort")
    @Mapping(source = "baseUdafParam", target = "baseUdafParam", qualifiedByName = {"BaseUdafParamMapstruct", "toCoreBaseUdafParam"})
    @Mapping(source = "mapUdafParam", target = "mapUdafParam", qualifiedByName = {"MapUdafParamMapstruct", "toCoreMapUdafParam"})
    MixUdafParamItem toCoreMixUdafParamItem(MixUdafParamItemEntity mixUdafParamItem);

}
