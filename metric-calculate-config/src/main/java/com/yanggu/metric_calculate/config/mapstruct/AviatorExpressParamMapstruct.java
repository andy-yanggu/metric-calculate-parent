package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.AviatorExpressParamDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunction;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstance;
import com.yanggu.metric_calculate.config.pojo.entity.JarStore;
import org.dromara.hutool.core.collection.CollUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("AviatorExpressParamMapstruct")
@Mapper(uses = {AviatorFunctionInstanceMapstruct.class}, componentModel = SPRING)
public interface AviatorExpressParamMapstruct extends BaseMapstruct<AviatorExpressParamDto, AviatorExpressParam> {

    @Named("toCoreAviatorExpressParam")
    @Mapping(source = "express", target = "express")
    @Mapping(source = "aviatorFunctionInstanceList", target = "aviatorFunctionInstanceList", qualifiedByName = {"AviatorFunctionInstanceMapstruct", "toCoreInstance"})
    com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam toCoreAviatorExpressParam(AviatorExpressParam param);

    static List<String> getAviatorFunctionJarPathList(List<AviatorExpressParam> aviatorExpressParamList) {
        return aviatorExpressParamList.stream()
                .map(AviatorExpressParam::getAviatorFunctionInstanceList)
                .filter(CollUtil::isNotEmpty)
                .flatMap(Collection::stream)
                .map(AviatorFunctionInstance::getAviatorFunction)
                .map(AviatorFunction::getJarStore)
                .filter(Objects::nonNull)
                .map(JarStore::getJarUrl)
                .distinct()
                .toList();
    }

}
