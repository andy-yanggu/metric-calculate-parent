package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionDTO;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AggregateFunctionMapstruct extends BaseMapstruct<AggregateFunctionDTO, AggregateFunctionEntity> {
}
