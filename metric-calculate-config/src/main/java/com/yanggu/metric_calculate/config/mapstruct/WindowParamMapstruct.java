package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.WindowParamDTO;
import com.yanggu.metric_calculate.config.pojo.entity.WindowParamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("WindowParamMapstruct")
@Mapper(
        uses = {
                ModelTimeColumnMapstruct.class,
                AviatorExpressParamMapstruct.class,
                NodePatternMapstruct.class
        },
        componentModel = SPRING)
public interface WindowParamMapstruct extends BaseMapstruct<WindowParamDTO, WindowParamEntity> {

    @Named("toCoreWindowParam")
    @Mapping(source = "modelTimeColumn", target = "timeColumn", qualifiedByName = {"ModelTimeColumnMapstruct", "toCoreTimeColumn"})
    @Mapping(source = "statusExpressParamList", target = "statusExpressParamList", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    @Mapping(source = "nodePatternList", target = "nodePatternList", qualifiedByName = {"NodePatternMapstruct", "toCoreNodePattern"})
    com.yanggu.metric_calculate.core.pojo.window.WindowParam toCoreWindowParam(WindowParamEntity windowParam);

}
