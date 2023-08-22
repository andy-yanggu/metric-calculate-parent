package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.Derive;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DeriveMapstruct extends BaseMapstruct<DeriveDto, Derive> {

    //DeriveMetrics toDeriveMetrics(DeriveDto deriveDto);

}
