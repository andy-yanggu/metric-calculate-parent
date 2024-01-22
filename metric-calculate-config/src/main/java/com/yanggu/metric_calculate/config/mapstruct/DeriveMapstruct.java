package com.yanggu.metric_calculate.config.mapstruct;


import com.yanggu.metric_calculate.config.pojo.dto.DeriveDTO;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.pojo.vo.DeriveMetricsConfigData;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import org.dromara.hutool.core.collection.CollUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("DeriveMapstruct")
@Mapper(
        uses = {
                ModelMapstruct.class,
                ModelDimensionColumnMapstruct.class,
                ModelTimeColumnMapstruct.class,
                AviatorExpressParamMapstruct.class,
                AggregateFunctionParamMapstruct.class,
                WindowParamMapstruct.class
        }, componentModel = SPRING
)
public interface DeriveMapstruct extends BaseMapstruct<DeriveDTO, DeriveEntity> {

    /**
     * 转换成core中的派生指标
     *
     * @param derive
     * @return
     */
    @Named("toDeriveMetrics")
    //维度字段
    @Mapping(source = "modelDimensionColumnList", target = "dimensionList", qualifiedByName = {"ModelDimensionColumnMapstruct", "toCoreDimension"})
    //时间字段
    @Mapping(source = "atom.modelTimeColumn", target = "timeColumn", qualifiedByName = {"ModelTimeColumnMapstruct", "toCoreTimeColumn"})
    //前置过滤条件
    @Mapping(source = "filterExpressParam", target = "filterExpressParam", qualifiedByName = {"AviatorExpressParamMapstruct", "toCoreAviatorExpressParam"})
    //聚合函数参数
    @Mapping(source = "atom.aggregateFunctionParam", target = "aggregateFunctionParam", qualifiedByName = {"AggregateFunctionParamMapstruct", "toCoreAggregateFunctionParam"})
    //窗口参数
    @Mapping(source = "windowParam", target = "windowParam", qualifiedByName = {"WindowParamMapstruct", "toCoreWindowParam"})
    //精度数据
    @Mapping(source = "roundAccuracyType", target = "roundAccuracy.type")
    @Mapping(source = "roundAccuracyLength", target = "roundAccuracy.length")
    DeriveMetrics toDeriveMetrics(DeriveEntity derive);

    /**
     * 转换成流计算和批计算中的派生指标配置类
     *
     * @param derive
     * @param model
     * @return
     */
    @Mapping(source = "derive", target = "deriveMetrics", qualifiedByName = "toDeriveMetrics")
    @Mapping(source = "model.modelColumnList", target = "fieldMap", qualifiedByName = {"ModelMapstruct", "getFieldMap"})
    @Mapping(source = "derive", target = "aviatorFunctionJarPathList", qualifiedByName = "getFromDerive")
    @Mapping(source = "derive.atom.aggregateFunctionParam", target = "udafJarPathList", qualifiedByName = {"AggregateFunctionParamMapstruct", "getUdafJarPathList"})
    DeriveMetricsConfigData toDeriveMetricsConfigData(DeriveEntity derive, ModelEntity model);

    /**
     * 尝试从派生指标中获取使用的表达式
     *
     * @param derive
     */
    static List<AviatorExpressParamEntity> getAviatorExpressParamFromDerive(DeriveEntity derive) {
        List<AviatorExpressParamEntity> aviatorExpressParamList = new ArrayList<>();
        //从前置过滤条件中尝试获取
        AviatorExpressParamEntity filterExpressParam = derive.getFilterExpressParam();
        if (filterExpressParam != null) {
            aviatorExpressParamList.add(filterExpressParam);
        }
        //从聚合函数参数尝试获取
        AggregateFunctionParamEntity aggregateFunctionParam = derive.getAtom().getAggregateFunctionParam();
        //基本聚合类型参数
        BaseUdafParamEntity baseUdafParam = aggregateFunctionParam.getBaseUdafParam();
        addAviatorExpressParamFromBaseUdafParam(baseUdafParam, aviatorExpressParamList);

        //映射类型参数
        MapUdafParamEntity mapUdafParam = aggregateFunctionParam.getMapUdafParam();
        if (mapUdafParam != null) {
            aviatorExpressParamList.addAll(mapUdafParam.getDistinctFieldParamList());
            addAviatorExpressParamFromBaseUdafParam(mapUdafParam.getValueAggParam(), aviatorExpressParamList);
        }

        //混合类型参数
        MixUdafParamEntity mixUdafParam = aggregateFunctionParam.getMixUdafParam();
        if (mixUdafParam != null) {
            mixUdafParam.getMixUdafParamItemList().stream()
                    .map(MixUdafParamItemEntity::getBaseUdafParam)
                    .forEach(temp -> addAviatorExpressParamFromBaseUdafParam(temp, aviatorExpressParamList));
            aviatorExpressParamList.add(mixUdafParam.getMetricExpressParam());
        }

        //从窗口参数尝试获取
        WindowParamEntity windowParam = derive.getWindowParam();
        List<NodePatternEntity> nodePatternList = windowParam.getNodePatternList();
        if (CollUtil.isNotEmpty(nodePatternList)) {
            List<AviatorExpressParamEntity> list = nodePatternList.stream()
                    .map(NodePatternEntity::getMatchExpressParam)
                    .toList();
            aviatorExpressParamList.addAll(list);
        }
        List<AviatorExpressParamEntity> statusExpressParamList = windowParam.getStatusExpressParamList();
        if (CollUtil.isNotEmpty(statusExpressParamList)) {
            aviatorExpressParamList.addAll(statusExpressParamList);
        }
        return aviatorExpressParamList;
    }

    /**
     * 尝试从派生指标中获取使用的自定义函数以的jar包路径
     *
     * @param derive
     */
    @Named("getFromDerive")
    static List<String> getFromDerive(DeriveEntity derive) {
        List<AviatorExpressParamEntity> aviatorExpressParamList = getAviatorExpressParamFromDerive(derive);
        return AviatorExpressParamMapstruct.getAviatorFunctionJarPathList(aviatorExpressParamList);
    }

    /**
     * 从基本聚合参数中获取
     *
     * @param baseUdafParam
     * @param aviatorExpressParamList
     */
    private static void addAviatorExpressParamFromBaseUdafParam(BaseUdafParamEntity baseUdafParam,
                                                                List<AviatorExpressParamEntity> aviatorExpressParamList) {
        if (baseUdafParam == null) {
            return;
        }
        AviatorExpressParamEntity metricExpressParam = baseUdafParam.getMetricExpressParam();
        if (metricExpressParam != null) {
            aviatorExpressParamList.add(metricExpressParam);
        }
        List<AviatorExpressParamEntity> metricExpressParamList = baseUdafParam.getMetricExpressParamList();
        if (CollUtil.isNotEmpty(metricExpressParamList)) {
            aviatorExpressParamList.addAll(metricExpressParamList);
        }
    }

}
