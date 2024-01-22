package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionDTO;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionEntity;
import com.yanggu.metric_calculate.config.pojo.vo.AggregateFunctionVO;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AggregateFunctionMapstruct extends BaseMapstruct<AggregateFunctionEntity, AggregateFunctionVO, AggregateFunctionDTO> {
}
