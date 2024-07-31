package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionParamMapper;
import com.yanggu.metric_calculate.config.domain.entity.*;
import com.yanggu.metric_calculate.config.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.domain.entity.table.AggregateFunctionParamBaseUdafParamRelationTableDef.AGGREGATE_FUNCTION_PARAM_BASE_UDAF_PARAM_RELATION;
import static com.yanggu.metric_calculate.config.domain.entity.table.AggregateFunctionParamMapUdafParamRelationTableDef.AGGREGATE_FUNCTION_PARAM_MAP_UDAF_PARAM_RELATION;
import static com.yanggu.metric_calculate.config.domain.entity.table.AggregateFunctionParamMixUdafParamRelationTableDef.AGGREGATE_FUNCTION_PARAM_MIX_UDAF_PARAM_RELATION;

/**
 * 聚合函数参数配置类 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class AggregateFunctionParamServiceImpl extends ServiceImpl<AggregateFunctionParamMapper, AggregateFunctionParamEntity> implements AggregateFunctionParamService {

    @Autowired
    private BaseUdafParamService baseUdafParamService;

    @Autowired
    private MapUdafParamService mapUdafParamService;

    @Autowired
    private MixUdafParamService mixUdafParamService;

    @Autowired
    private AggregateFunctionParamBaseUdafParamRelationService aggregateFunctionParamBaseUdafParamRelationService;

    @Autowired
    private AggregateFunctionParamMapUdafParamRelationService aggregateFunctionParamMapUdafParamRelationService;

    @Autowired
    private AggregateFunctionParamMixUdafParamRelationService aggregateFunctionParamMixUdafParamRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(AggregateFunctionParamEntity aggregateFunctionParam,
                         List<ModelColumnEntity> modelColumnList) throws Exception {
        super.save(aggregateFunctionParam);
        //基本配置类型
        BaseUdafParamEntity baseUdafParam = aggregateFunctionParam.getBaseUdafParam();
        if (baseUdafParam != null) {
            baseUdafParamService.saveData(baseUdafParam, modelColumnList);
            AggregateFunctionParamBaseUdafParamRelationEntity relation = new AggregateFunctionParamBaseUdafParamRelationEntity();
            relation.setAggregateFunctionParamId(aggregateFunctionParam.getId());
            relation.setBaseUdafParamId(baseUdafParam.getId());
            aggregateFunctionParamBaseUdafParamRelationService.save(relation);
        }
        //映射类型
        MapUdafParamEntity mapUdafParam = aggregateFunctionParam.getMapUdafParam();
        if (mapUdafParam != null) {
            mapUdafParamService.saveData(mapUdafParam, modelColumnList);
            AggregateFunctionParamMapUdafParamRelationEntity relation = new AggregateFunctionParamMapUdafParamRelationEntity();
            relation.setAggregateFunctionParamId(aggregateFunctionParam.getId());
            relation.setMapUdafParamId(mapUdafParam.getId());
            aggregateFunctionParamMapUdafParamRelationService.save(relation);
        }
        //混合类型
        MixUdafParamEntity mixUdafParam = aggregateFunctionParam.getMixUdafParam();
        if (mixUdafParam != null) {
            mixUdafParamService.saveData(mixUdafParam, modelColumnList);
            AggregateFunctionParamMixUdafParamRelationEntity relation = new AggregateFunctionParamMixUdafParamRelationEntity();
            relation.setAggregateFunctionParamId(aggregateFunctionParam.getId());
            relation.setMixUdafParamId(mixUdafParam.getId());
            aggregateFunctionParamMixUdafParamRelationService.save(relation);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(AggregateFunctionParamEntity aggregateFunctionParam) {
        Integer aggregateFunctionParamId = aggregateFunctionParam.getId();
        super.removeById(aggregateFunctionParamId);

        BaseUdafParamEntity baseUdafParam = aggregateFunctionParam.getBaseUdafParam();
        if (baseUdafParam != null) {
            baseUdafParamService.deleteData(baseUdafParam);
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(AGGREGATE_FUNCTION_PARAM_BASE_UDAF_PARAM_RELATION.AGGREGATE_FUNCTION_PARAM_ID.eq(aggregateFunctionParamId))
                    .and(AGGREGATE_FUNCTION_PARAM_BASE_UDAF_PARAM_RELATION.BASE_UDAF_PARAM_ID.eq(baseUdafParam.getId()));
            aggregateFunctionParamBaseUdafParamRelationService.remove(queryWrapper);
        }
        MapUdafParamEntity mapUdafParam = aggregateFunctionParam.getMapUdafParam();
        if (mapUdafParam != null) {
            mapUdafParamService.deleteData(mapUdafParam);
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(AGGREGATE_FUNCTION_PARAM_MAP_UDAF_PARAM_RELATION.AGGREGATE_FUNCTION_PARAM_ID.eq(aggregateFunctionParamId))
                    .and(AGGREGATE_FUNCTION_PARAM_MAP_UDAF_PARAM_RELATION.MAP_UDAF_PARAM_ID.eq(mapUdafParam.getId()));
            aggregateFunctionParamMapUdafParamRelationService.remove(queryWrapper);
        }
        MixUdafParamEntity mixUdafParam = aggregateFunctionParam.getMixUdafParam();
        if (mixUdafParam != null) {
            mixUdafParamService.deleteData(mixUdafParam);
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(AGGREGATE_FUNCTION_PARAM_MIX_UDAF_PARAM_RELATION.AGGREGATE_FUNCTION_PARAM_ID.eq(aggregateFunctionParamId))
                    .and(AGGREGATE_FUNCTION_PARAM_MIX_UDAF_PARAM_RELATION.MIX_UDAF_PARAM_ID.eq(mixUdafParam.getId()));
            aggregateFunctionParamMixUdafParamRelationService.remove(queryWrapper);
        }
    }

}