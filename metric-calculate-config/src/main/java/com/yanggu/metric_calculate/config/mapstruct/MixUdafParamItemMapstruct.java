package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.MixUdafParamItemDTO;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("MixUdafParamItemMapstruct")
@Mapper(uses = {BaseUdafParamMapstruct.class, MapUdafParamMapstruct.class}, componentModel = SPRING)
public interface MixUdafParamItemMapstruct extends BaseMapstruct<MixUdafParamItemDTO, MixUdafParamItemEntity> {

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
    com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParamItem toCoreMixUdafParamItem(MixUdafParamItemEntity mixUdafParamItem);

}
