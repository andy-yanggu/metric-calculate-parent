package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import org.dromara.hutool.core.collection.CollUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(uses = {ModelColumnMapstruct.class, DeriveMapstruct.class}, componentModel = SPRING)
public interface ModelMapstruct extends BaseMapstruct<ModelDto, Model> {

    //宽表字段
    @Mapping(source = "modelColumnList", target = "modelColumnList", qualifiedByName = {"ModelColumnMapstruct", "toCoreModelColumn"})
    //派生指标
    @Mapping(source = "deriveList", target = "deriveMetricsList", qualifiedByName = {"DeriveMapstruct", "toDeriveMetrics"})
    //自定义udaf的jar包路径
    @Mapping(source = "deriveList", target = "udafJarPathList", qualifiedByName = "getUdafJarPathList")
    com.yanggu.metric_calculate.core.pojo.data_detail_table.Model toCoreModel(Model model);

    @Named("getUdafJarPathList")
    default List<String> getUdafJarPathList(List<Derive> deriveList) {
        if (CollUtil.isEmpty(deriveList)) {
            return Collections.emptyList();
        }
        return deriveList.stream()
                .map(Derive::getAggregateFunctionParam)
                .map(AggregateFunctionParam::getAggregateFunction)
                .map(AggregateFunction::getJarStore)
                .filter(Objects::nonNull)
                .map(JarStore::getJarUrl)
                .distinct()
                .toList();
    }

}