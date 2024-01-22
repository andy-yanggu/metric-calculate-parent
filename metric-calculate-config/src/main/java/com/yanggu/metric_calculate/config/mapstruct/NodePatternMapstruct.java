package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.NodePatternDTO;
import com.yanggu.metric_calculate.config.pojo.entity.NodePatternEntity;
import com.yanggu.metric_calculate.config.pojo.vo.NodePatternVO;
import com.yanggu.metric_calculate.core.pojo.window.NodePattern;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("NodePatternMapstruct")
@Mapper(uses = {AviatorExpressParamMapstruct.class}, componentModel = SPRING)
public interface NodePatternMapstruct extends BaseMapstruct<NodePatternEntity, NodePatternVO, NodePatternDTO> {

    @Named("toCoreNodePattern")
    @Mapping(source = "matchExpressParam", target = "matchExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    NodePattern toCoreNodePattern(NodePatternEntity nodePattern);

}
