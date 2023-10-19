package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.ModelDimensionColumnDto;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelDimensionColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("ModelDimensionColumnMapstruct")
@Mapper(componentModel = SPRING)
public interface ModelDimensionColumnMapstruct extends BaseMapstruct<ModelDimensionColumnDto, com.yanggu.metric_calculate.config.pojo.entity.ModelDimensionColumn> {

    @Named("toCoreDimension")
    @Mapping(source = "modelColumnName", target = "columnName")
    @Mapping(source = "dimensionName", target = "dimensionName")
    @Mapping(source = "sort", target = "columnIndex")
    ModelDimensionColumn toCoreDimension(com.yanggu.metric_calculate.config.pojo.entity.ModelDimensionColumn modelDimensionColumn);

}
