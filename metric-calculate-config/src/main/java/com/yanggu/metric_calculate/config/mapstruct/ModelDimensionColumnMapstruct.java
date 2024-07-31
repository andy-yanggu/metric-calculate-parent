package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.domain.dto.ModelDimensionColumnDTO;
import com.yanggu.metric_calculate.config.domain.entity.ModelDimensionColumnEntity;
import com.yanggu.metric_calculate.config.domain.vo.ModelDimensionColumnVO;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelDimensionColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("ModelDimensionColumnMapstruct")
@Mapper(componentModel = SPRING)
public interface ModelDimensionColumnMapstruct extends BaseMapstruct<ModelDimensionColumnEntity, ModelDimensionColumnVO, ModelDimensionColumnDTO> {

    @Named("toCoreDimension")
    @Mapping(source = "modelColumnName", target = "columnName")
    @Mapping(source = "dimensionName", target = "dimensionName")
    @Mapping(source = "sort", target = "columnIndex")
    ModelDimensionColumn toCoreDimension(ModelDimensionColumnEntity modelDimensionColumn);

}
