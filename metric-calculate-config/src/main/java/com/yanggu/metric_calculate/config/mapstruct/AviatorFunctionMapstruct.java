package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.domain.dto.AviatorFunctionDTO;
import com.yanggu.metric_calculate.config.domain.entity.AviatorFunctionEntity;
import com.yanggu.metric_calculate.config.domain.vo.AviatorFunctionVO;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AviatorFunctionMapstruct extends BaseMapstruct<AviatorFunctionEntity, AviatorFunctionVO, AviatorFunctionDTO> {
}
