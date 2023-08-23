package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.ModelDimensionColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.ModelDimensionColumn;
import com.yanggu.metric_calculate.core.pojo.metric.Dimension;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("ModelDimensionColumnMapstruct")
@Mapper(componentModel = SPRING)
public interface ModelDimensionColumnMapstruct extends BaseMapstruct<ModelDimensionColumnDto, ModelDimensionColumn> {

    @Named("toCoreDimension")
    @Mapping(source = "modelColumn.name", target = "columnName")
    @Mapping(source = "dimension.name", target = "dimensionName")
    @Mapping(source = "sort", target = "columnIndex")
    Dimension toCoreDimension(ModelDimensionColumn modelDimensionColumn);

}
