package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.domain.dto.AviatorExpressParamDTO;
import com.yanggu.metric_calculate.config.domain.entity.AviatorExpressParamEntity;
import com.yanggu.metric_calculate.config.domain.entity.AviatorFunctionEntity;
import com.yanggu.metric_calculate.config.domain.entity.AviatorFunctionInstanceEntity;
import com.yanggu.metric_calculate.config.domain.entity.JarStoreEntity;
import com.yanggu.metric_calculate.config.domain.vo.AviatorExpressParamVO;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
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
public interface AviatorExpressParamMapstruct extends BaseMapstruct<AviatorExpressParamEntity, AviatorExpressParamVO, AviatorExpressParamDTO> {

    /**
     * 转换成core中的AviatorExpressParam
     *
     * @param param
     * @return
     */
    @Named("toCoreAviatorExpressParam")
    @Mapping(source = "express", target = "express")
    @Mapping(source = "aviatorFunctionInstanceList", target = "aviatorFunctionInstanceList", qualifiedByName = {"AviatorFunctionInstanceMapstruct", "toCoreInstance"})
    AviatorExpressParam toCoreAviatorExpressParam(AviatorExpressParamEntity param);

    /**
     * 从AviatorExpressParam表达式中获取自定义的aviator函数jar包路径
     *
     * @param aviatorExpressParamList
     * @return
     */
    static List<String> getAviatorFunctionJarPathList(List<AviatorExpressParamEntity> aviatorExpressParamList) {
        return aviatorExpressParamList.stream()
                .map(AviatorExpressParamEntity::getAviatorFunctionInstanceList)
                .filter(CollUtil::isNotEmpty)
                .flatMap(Collection::stream)
                .map(AviatorFunctionInstanceEntity::getAviatorFunction)
                .map(AviatorFunctionEntity::getJarStore)
                .filter(Objects::nonNull)
                .map(JarStoreEntity::getJarUrl)
                .distinct()
                .toList();
    }

}
