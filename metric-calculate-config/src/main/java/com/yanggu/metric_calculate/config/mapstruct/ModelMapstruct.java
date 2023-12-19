package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.core.enums.BasicType;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("ModelMapstruct")
@Mapper(uses = {ModelColumnMapstruct.class, DeriveMapstruct.class}, componentModel = SPRING)
public interface ModelMapstruct extends BaseMapstruct<ModelDto, Model> {

    /**
     * 转换成core中的宽表
     *
     * @param model
     * @return
     */
    @Named("toCoreModel")
    //宽表字段
    @Mapping(source = "modelColumnList", target = "modelColumnList", qualifiedByName = {"ModelColumnMapstruct", "toCoreModelColumn"})
    //派生指标
    @Mapping(source = "deriveList", target = "deriveMetricsList", qualifiedByName = {"DeriveMapstruct", "toDeriveMetrics"})
    //自定义udaf的jar包路径
    @Mapping(source = "atomList", target = "udafJarPathList", qualifiedByName = "getUdafJarPathList")
    //自定义aviator函数jar包路径
    @Mapping(source = "model", target = "aviatorFunctionJarPathList", qualifiedByName = "getAviatorFunctionJarPathList")
    com.yanggu.metric_calculate.core.pojo.data_detail_table.Model toCoreModel(Model model);

    @IterableMapping(qualifiedByName = "toCoreModel")
    List<com.yanggu.metric_calculate.core.pojo.data_detail_table.Model> toCoreModel(List<Model> modelList);

    @Named("getFieldMap")
    default Map<String, Class<?>> getFieldMap(List<ModelColumn> modelColumnList) {
        List<com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelColumn> collect =
                modelColumnList.stream()
                        .map(tempModelColumn -> {
                            com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelColumn modelColumn = new com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelColumn();
                            modelColumn.setName(tempModelColumn.getName());
                            modelColumn.setDataType(BasicType.valueOf(tempModelColumn.getDataType().name()));
                            return modelColumn;
                        })
                        .toList();
        return MetricUtil.getFieldMap(collect);
    }

    /**
     * 获取自定义聚合函数jar包路径
     *
     * @param atomList
     * @return
     */
    @Named("getUdafJarPathList")
    default List<String> getUdafJarPathList(List<Atom> atomList) {
        if (CollUtil.isEmpty(atomList)) {
            return Collections.emptyList();
        }
        return atomList.stream()
                .map(Atom::getAggregateFunctionParam)
                .map(AggregateFunctionParamMapstruct::getUdafJarPathList)
                .filter(CollUtil::isNotEmpty)
                .flatMap(Collection::stream)
                .distinct()
                .toList();
    }

    /**
     * 获取自定义Aviator函数jar包路径
     *
     * @param model
     * @return
     */
    @Named("getAviatorFunctionJarPathList")
    default List<String> getAviatorFunctionJarPathList(Model model) {
        List<AviatorExpressParam> aviatorExpressParamList = new ArrayList<>();
        //从宽表虚拟字段表达式尝试获取自定义Aviator函数jar包路径
        List<ModelColumn> modelColumnList = model.getModelColumnList();
        if (CollUtil.isNotEmpty(modelColumnList)) {
            List<AviatorExpressParam> list = modelColumnList.stream()
                    .map(ModelColumn::getAviatorExpressParam)
                    .filter(Objects::nonNull)
                    .toList();
            if (CollUtil.isNotEmpty(list)) {
                aviatorExpressParamList.addAll(list);
            }
        }
        List<Derive> deriveList = model.getDeriveList();
        if (CollUtil.isNotEmpty(deriveList)) {
            //尝试从派生指标中获取
            for (Derive derive : deriveList) {
                List<AviatorExpressParam> tempList = DeriveMapstruct.getAviatorExpressParamFromDerive(derive);
                if (CollUtil.isNotEmpty(tempList)) {
                    aviatorExpressParamList.addAll(tempList);
                }
            }
        }
        return AviatorExpressParamMapstruct.getAviatorFunctionJarPathList(aviatorExpressParamList);
    }

}