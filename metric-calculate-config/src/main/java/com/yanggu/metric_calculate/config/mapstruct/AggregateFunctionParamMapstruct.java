package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.enums.AggregateFunctionTypeEnums;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionParamDTO;
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
public interface AggregateFunctionParamMapstruct extends BaseMapstruct<AggregateFunctionParamDTO, AggregateFunctionParamEntity> {

    /**
     * 转换成core中的类
     *
     * @param aggregateFunctionParam
     * @return
     */
    @Named("toCoreAggregateFunctionParam")
    @Mapping(source = "aggregateFunction.name", target = "aggregateType")
    //基本聚合函数
    @Mapping(source = "baseUdafParam", target = "baseUdafParam", qualifiedByName = {"BaseUdafParamMapstruct", "toCoreBaseUdafParam"})
    //映射类型参数
    @Mapping(source = "mapUdafParam", target = "mapUdafParam", qualifiedByName = {"MapUdafParamMapstruct", "toCoreMapUdafParam"})
    //混合型参数
    @Mapping(source = "mixUdafParam", target = "mixUdafParam", qualifiedByName = {"MixUdafParamMapstruct", "toCoreMixUdafParam"})
    com.yanggu.metric_calculate.core.pojo.udaf_param.AggregateFunctionParam toCoreAggregateFunctionParam(AggregateFunctionParamEntity aggregateFunctionParam);

    /**
     * 获取udaf的jar包路径
     *
     * @param aggregateFunctionParam
     * @return
     */
    @Named("getUdafJarPathList")
    static List<String> getUdafJarPathList(AggregateFunctionParamEntity aggregateFunctionParam) {
        if (aggregateFunctionParam == null) {
            return Collections.emptyList();
        }
        List<AggregateFunctionEntity> aggregateFunctionList = new ArrayList<>();
        AggregateFunctionEntity aggregateFunction = aggregateFunctionParam.getAggregateFunction();
        AggregateFunctionTypeEnums aggregateFunctionType = aggregateFunction.getType();
        //基本类型
        if (AggregateFunctionTypeEnums.isBasicType(aggregateFunctionType)) {
            BaseUdafParamEntity baseUdafParam = aggregateFunctionParam.getBaseUdafParam();
            aggregateFunctionList.add(baseUdafParam.getAggregateFunction());
        } else if (MAP_TYPE.equals(aggregateFunctionType)) {
            //映射类型
            MapUdafParamEntity mapUdafParam = aggregateFunctionParam.getMapUdafParam();
            aggregateFunctionList.add(mapUdafParam.getAggregateFunction());
            aggregateFunctionList.add(mapUdafParam.getValueAggParam().getAggregateFunction());
        } else if (MIX.equals(aggregateFunctionType)) {
            //混合类型
            MixUdafParamEntity mixUdafParam = aggregateFunctionParam.getMixUdafParam();
            aggregateFunctionList.add(mixUdafParam.getAggregateFunction());
            List<AggregateFunctionEntity> list = mixUdafParam.getMixUdafParamItemList().stream()
                    .map(MixUdafParamItemEntity::getBaseUdafParam)
                    .map(BaseUdafParamEntity::getAggregateFunction)
                    .toList();
            aggregateFunctionList.addAll(list);
        } else {
            throw new BusinessException(AGGREGATE_FUNCTION_TYPE_ERROR);
        }
        return aggregateFunctionList.stream()
                .map(AggregateFunctionEntity::getJarStore)
                .filter(Objects::nonNull)
                .map(JarStoreEntity::getJarUrl)
                .distinct()
                .toList();
    }

}
