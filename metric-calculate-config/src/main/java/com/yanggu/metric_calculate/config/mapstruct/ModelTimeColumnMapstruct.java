package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.ModelTimeColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.ModelTimeColumn;
import com.yanggu.metric_calculate.core.pojo.metric.TimeColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("ModelTimeColumnMapstruct")
@Mapper(componentModel = SPRING)
public interface ModelTimeColumnMapstruct extends BaseMapstruct<ModelTimeColumnDto, ModelTimeColumn> {

    @Named("toCoreTimeColumn")
    @Mapping(source = "modelColumn.name", target = "columnName")
    @Mapping(source = "timeFormat", target = "timeFormat")
    TimeColumn toCoreTimeColumn(ModelTimeColumn modelTimeColumn);

}
