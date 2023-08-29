package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import org.dromara.hutool.core.collection.CollUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(uses = {ModelColumnMapstruct.class, DeriveMapstruct.class}, componentModel = SPRING)
public interface ModelMapstruct extends BaseMapstruct<ModelDto, Model> {

    //宽表字段
    @Mapping(source = "modelColumnList", target = "modelColumnList", qualifiedByName = {"ModelColumnMapstruct", "toCoreModelColumn"})
    //派生指标
    @Mapping(source = "deriveList", target = "deriveMetricsList", qualifiedByName = {"DeriveMapstruct", "toDeriveMetrics"})
    //自定义udaf的jar包路径
    @Mapping(source = "deriveList", target = "udafJarPathList", qualifiedByName = "getUdafJarPathList")
    //自定义aviator函数jar包路径
    @Mapping(source = "model", target = "aviatorFunctionJarPathList", qualifiedByName = "getAviatorFunctionJarPathList")
    com.yanggu.metric_calculate.core.pojo.data_detail_table.Model toCoreModel(Model model);

    /**
     * 获取自定义聚合函数jar包路径
     *
     * @param deriveList
     * @return
     */
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
                //从前置过滤条件中尝试获取
                AviatorExpressParam filterExpressParam = derive.getFilterExpressParam();
                if (filterExpressParam != null) {
                    aviatorExpressParamList.add(filterExpressParam);
                }
                //从聚合函数参数尝试获取
                AggregateFunctionParam aggregateFunctionParam = derive.getAggregateFunctionParam();
                //基本聚合类型参数
                BaseUdafParam baseUdafParam = aggregateFunctionParam.getBaseUdafParam();
                addAviatorExpressParamFromBaseUdafParam(baseUdafParam, aviatorExpressParamList);

                //映射类型参数
                MapUdafParam mapUdafParam = aggregateFunctionParam.getMapUdafParam();
                if (mapUdafParam != null) {
                    aviatorExpressParamList.addAll(mapUdafParam.getDistinctFieldParamList());
                    addAviatorExpressParamFromBaseUdafParam(mapUdafParam.getValueAggParam(), aviatorExpressParamList);
                }

                //混合类型参数
                MixUdafParam mixUdafParam = aggregateFunctionParam.getMixUdafParam();
                if (mixUdafParam != null) {
                    mixUdafParam.getMixUdafParamItemList().stream()
                            .map(MixUdafParamItem::getBaseUdafParam)
                            .forEach(temp -> addAviatorExpressParamFromBaseUdafParam(temp, aviatorExpressParamList));
                    aviatorExpressParamList.add(mixUdafParam.getMetricExpressParam());
                }

                //从窗口参数尝试获取
                WindowParam windowParam = derive.getWindowParam();
                List<NodePattern> nodePatternList = windowParam.getNodePatternList();
                if (CollUtil.isNotEmpty(nodePatternList)) {
                    List<AviatorExpressParam> list = nodePatternList.stream()
                            .map(NodePattern::getMatchExpressParam)
                            .toList();
                    aviatorExpressParamList.addAll(list);
                }
                List<AviatorExpressParam> statusExpressParamList = windowParam.getStatusExpressParamList();
                if (CollUtil.isNotEmpty(statusExpressParamList)) {
                    aviatorExpressParamList.addAll(statusExpressParamList);
                }
            }
        }
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

    private void addAviatorExpressParamFromBaseUdafParam(BaseUdafParam baseUdafParam,
                                                         List<AviatorExpressParam> aviatorExpressParamList) {
        if (baseUdafParam == null) {
            return;
        }
        AviatorExpressParam metricExpressParam = baseUdafParam.getMetricExpressParam();
        if (metricExpressParam != null) {
            aviatorExpressParamList.add(metricExpressParam);
        }
        List<AviatorExpressParam> metricExpressParamList = baseUdafParam.getMetricExpressParamList();
        if (CollUtil.isNotEmpty(metricExpressParamList)) {
            aviatorExpressParamList.addAll(metricExpressParamList);
        }
        AviatorExpressParam retainExpressParam = baseUdafParam.getRetainExpressParam();
        if (retainExpressParam != null) {
            aviatorExpressParamList.add(retainExpressParam);
        }
        List<AviatorExpressParam> objectiveCompareFieldParamList = baseUdafParam.getObjectiveCompareFieldParamList();
        if (CollUtil.isNotEmpty(objectiveCompareFieldParamList)) {
            aviatorExpressParamList.addAll(objectiveCompareFieldParamList);
        }
        List<FieldOrderParam> collectiveSortFieldList = baseUdafParam.getCollectiveSortFieldList();
        if (CollUtil.isNotEmpty(collectiveSortFieldList)) {
            List<AviatorExpressParam> list = collectiveSortFieldList.stream()
                    .map(FieldOrderParam::getAviatorExpressParam)
                    .toList();
            aviatorExpressParamList.addAll(list);
        }
        List<AviatorExpressParam> distinctFieldListParamList = baseUdafParam.getDistinctFieldListParamList();
        if (CollUtil.isNotEmpty(distinctFieldListParamList)) {
            aviatorExpressParamList.addAll(distinctFieldListParamList);
        }
    }

}