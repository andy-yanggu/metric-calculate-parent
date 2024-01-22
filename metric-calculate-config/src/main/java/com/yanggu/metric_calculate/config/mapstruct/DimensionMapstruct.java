package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.DimensionDTO;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionEntity;
import com.yanggu.metric_calculate.config.pojo.vo.DimensionVO;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DimensionMapstruct extends BaseMapstruct<DimensionEntity, DimensionVO, DimensionDTO> {
}
