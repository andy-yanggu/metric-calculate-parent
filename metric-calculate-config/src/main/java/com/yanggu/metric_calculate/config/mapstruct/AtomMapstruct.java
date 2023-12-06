package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.AtomDto;
import com.yanggu.metric_calculate.config.pojo.entity.Atom;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AtomMapstruct extends BaseMapstruct<AtomDto, Atom> {
}
