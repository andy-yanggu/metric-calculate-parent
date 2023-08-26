package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 聚合函数参数配置类 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class AggregateFunctionParamServiceImpl extends ServiceImpl<AggregateFunctionParamMapper, AggregateFunctionParam> implements AggregateFunctionParamService {

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
    public void saveData(AggregateFunctionParam aggregateFunctionParam) throws Exception {
        super.save(aggregateFunctionParam);
        //基本配置类型
        BaseUdafParam baseUdafParam = aggregateFunctionParam.getBaseUdafParam();
        if (baseUdafParam != null) {
            baseUdafParamService.saveData(baseUdafParam);
            AggregateFunctionParamBaseUdafParamRelation relation = new AggregateFunctionParamBaseUdafParamRelation();
            relation.setAggregateFunctionParamId(aggregateFunctionParam.getId());
            relation.setBaseUdafParamId(baseUdafParam.getId());
            aggregateFunctionParamBaseUdafParamRelationService.save(relation);
        }
        //映射类型
        MapUdafParam mapUdafParam = aggregateFunctionParam.getMapUdafParam();
        if (mapUdafParam != null) {
            mapUdafParamService.saveData(mapUdafParam);
            AggregateFunctionParamMapUdafParamRelation relation = new AggregateFunctionParamMapUdafParamRelation();
            relation.setAggregateFunctionParamId(aggregateFunctionParam.getId());
            relation.setMapUdafParamId(mapUdafParam.getId());
            aggregateFunctionParamMapUdafParamRelationService.save(relation);
        }
        //混合类型
        MixUdafParam mixUdafParam = aggregateFunctionParam.getMixUdafParam();
        if (mixUdafParam != null) {
            mixUdafParamService.saveData(mixUdafParam);
            AggregateFunctionParamMixUdafParamRelation relation = new AggregateFunctionParamMixUdafParamRelation();
            relation.setAggregateFunctionParamId(aggregateFunctionParam.getId());
            relation.setMixUdafParamId(mixUdafParam.getId());
            aggregateFunctionParamMixUdafParamRelationService.save(relation);
        }
    }

}