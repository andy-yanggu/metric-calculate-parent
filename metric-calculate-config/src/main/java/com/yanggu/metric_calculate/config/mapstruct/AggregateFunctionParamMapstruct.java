package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.enums.AggregateFunctionTypeEnums;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionParamDto;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.yanggu.metric_calculate.config.enums.AggregateFunctionTypeEnums.MAP_TYPE;
import static com.yanggu.metric_calculate.config.enums.AggregateFunctionTypeEnums.MIX;
import static com.yanggu.metric_calculate.config.enums.ResultCode.AGGREGATE_FUNCTION_TYPE_ERROR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("AggregateFunctionParamMapstruct")
@Mapper(
        uses = {
                BaseUdafParamMapstruct.class,
                MapUdafParamMapstruct.class,
                MixUdafParamMapstruct.class
        },
        componentModel = SPRING)
public interface AggregateFunctionParamMapstruct extends BaseMapstruct<AggregateFunctionParamDto, AggregateFunctionParam> {

    /**
     * 转换成core中的类
     *
     * @param aggregateFunctionParam
     * @return
     */
    @Named("toCoreAggregateFunctionParam")
    @Mapping(source = "aggregateFunction.name", target = "aggregateType")
    @Mapping(source = "baseUdafParam", target = "baseUdafParam", qualifiedByName = {"BaseUdafParamMapstruct", "toCoreBaseUdafParam"})
    @Mapping(source = "mapUdafParam", target = "mapUdafParam", qualifiedByName = {"MapUdafParamMapstruct", "toCoreMapUdafParam"})
    @Mapping(source = "mixUdafParam", target = "mixUdafParam", qualifiedByName = {"MixUdafParamMapstruct", "toCoreMixUdafParam"})
    com.yanggu.metric_calculate.core.pojo.metric.AggregateFunctionParam toCoreAggregateFunctionParam(AggregateFunctionParam aggregateFunctionParam);

    /**
     * 获取udaf的jar包路径
     *
     * @param aggregateFunctionParam
     * @return
     */
    @Named("getUdafJarPathList")
    static List<String> getUdafJarPathList(AggregateFunctionParam aggregateFunctionParam) {
        if (aggregateFunctionParam == null) {
            return Collections.emptyList();
        }
        List<AggregateFunction> aggregateFunctionList = new ArrayList<>();
        AggregateFunction aggregateFunction = aggregateFunctionParam.getAggregateFunction();
        AggregateFunctionTypeEnums aggregateFunctionType = aggregateFunction.getType();
        //基本类型
        if (AggregateFunctionTypeEnums.isBasicType(aggregateFunctionType)) {
            BaseUdafParam baseUdafParam = aggregateFunctionParam.getBaseUdafParam();
            aggregateFunctionList.add(baseUdafParam.getAggregateFunction());
            aggregateFunctionList.add(baseUdafParam.getAggregateFunction());
        } else if (MAP_TYPE.equals(aggregateFunctionType)) {
            //映射类型
            MapUdafParam mapUdafParam = aggregateFunctionParam.getMapUdafParam();
            aggregateFunctionList.add(mapUdafParam.getAggregateFunction());
            aggregateFunctionList.add(mapUdafParam.getValueAggParam().getAggregateFunction());
        } else if (MIX.equals(aggregateFunctionType)) {
            //混合类型
            MixUdafParam mixUdafParam = aggregateFunctionParam.getMixUdafParam();
            aggregateFunctionList.add(mixUdafParam.getAggregateFunction());
            List<AggregateFunction> list = mixUdafParam.getMixUdafParamItemList().stream().map(MixUdafParamItem::getBaseUdafParam).map(BaseUdafParam::getAggregateFunction).toList();
            aggregateFunctionList.addAll(list);
        } else {
            throw new BusinessException(AGGREGATE_FUNCTION_TYPE_ERROR);
        }
        return aggregateFunctionList.stream()
                .map(AggregateFunction::getJarStore)
                .filter(Objects::nonNull)
                .map(JarStore::getJarUrl)
                .distinct()
                .toList();
    }

}
