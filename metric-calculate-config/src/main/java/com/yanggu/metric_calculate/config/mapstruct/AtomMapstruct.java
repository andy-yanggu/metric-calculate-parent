package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.domain.dto.AtomDTO;
import com.yanggu.metric_calculate.config.domain.entity.AtomEntity;
import com.yanggu.metric_calculate.config.domain.vo.AtomVO;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AtomMapstruct extends BaseMapstruct<AtomEntity, AtomVO, AtomDTO> {
}
