package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.DeriveModelDimensionColumnRelationDto;
import com.yanggu.metric_calculate.config.pojo.entity.DeriveModelDimensionColumnRelation;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DeriveModelDimensionColumnRelationMapstruct extends BaseMapstruct<DeriveModelDimensionColumnRelationDto, DeriveModelDimensionColumnRelation> {
}
