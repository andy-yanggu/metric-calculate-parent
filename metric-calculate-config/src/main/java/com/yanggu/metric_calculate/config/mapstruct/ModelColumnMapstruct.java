package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.ModelColumnDTO;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumnEntity;
import com.yanggu.metric_calculate.config.pojo.vo.ModelColumnVO;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("ModelColumnMapstruct")
@Mapper(uses = AviatorExpressParamMapstruct.class, componentModel = SPRING)
public interface ModelColumnMapstruct extends BaseMapstruct<ModelColumnEntity, ModelColumnVO, ModelColumnDTO> {

    @Named("toCoreModelColumn")
    @Mapping(source = "aviatorExpressParam", target = "aviatorExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    ModelColumn toCoreModelColumn(ModelColumnEntity modelColumn);

}
