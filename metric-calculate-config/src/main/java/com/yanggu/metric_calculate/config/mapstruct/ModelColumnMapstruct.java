package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.pojo.dto.ModelColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("ModelColumnMapstruct")
@Mapper(uses = AviatorExpressParamMapstruct.class, componentModel = SPRING)
public interface ModelColumnMapstruct extends BaseMapstruct<ModelColumnDto, ModelColumn> {

    @Named("toCoreModelColumn")
    @Mapping(source = "aviatorExpressParam", target = "aviatorExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelColumn toCoreModelColumn(ModelColumn modelColumn);

}
