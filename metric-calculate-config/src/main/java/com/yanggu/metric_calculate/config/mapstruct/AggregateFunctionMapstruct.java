package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.domain.dto.AggregateFunctionDTO;
import com.yanggu.metric_calculate.config.domain.entity.AggregateFunctionEntity;
import com.yanggu.metric_calculate.config.domain.vo.AggregateFunctionVO;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AggregateFunctionMapstruct extends BaseMapstruct<AggregateFunctionEntity, AggregateFunctionVO, AggregateFunctionDTO> {
}
