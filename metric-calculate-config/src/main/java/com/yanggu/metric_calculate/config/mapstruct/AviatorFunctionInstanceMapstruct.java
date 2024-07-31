package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.domain.dto.AviatorFunctionInstanceDTO;
import com.yanggu.metric_calculate.config.domain.entity.AviatorFunctionInstanceEntity;
import com.yanggu.metric_calculate.config.domain.vo.AviatorFunctionInstanceVO;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorFunctionInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("AviatorFunctionInstanceMapstruct")
@Mapper(componentModel = SPRING)
public interface AviatorFunctionInstanceMapstruct extends BaseMapstruct<AviatorFunctionInstanceEntity, AviatorFunctionInstanceVO, AviatorFunctionInstanceDTO> {

    /**
     * 转成成core中的AviatorFunctionInstance
     *
     * @param instance
     * @return
     */
    @Named("toCoreInstance")
    @Mapping(source = "aviatorFunction.name", target = "name")
    @Mapping(source = "param", target = "param")
    AviatorFunctionInstance toCoreInstance(AviatorFunctionInstanceEntity instance);

}
