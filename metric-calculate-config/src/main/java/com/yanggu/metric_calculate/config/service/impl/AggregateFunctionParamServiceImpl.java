package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionParam;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionParamBaseUdafParamRelation;
import com.yanggu.metric_calculate.config.pojo.entity.BaseUdafParam;
import com.yanggu.metric_calculate.config.service.AggregateFunctionParamBaseUdafParamRelationService;
import com.yanggu.metric_calculate.config.service.AggregateFunctionParamService;
import com.yanggu.metric_calculate.config.service.BaseUdafParamService;
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
    private AggregateFunctionParamBaseUdafParamRelationService aggregateFunctionParamBaseUdafParamRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean save(AggregateFunctionParam aggregateFunctionParam) {
        super.save(aggregateFunctionParam);
        BaseUdafParam baseUdafParam = aggregateFunctionParam.getBaseUdafParam();
        if (baseUdafParam != null) {
            baseUdafParam.setUserId(aggregateFunctionParam.getUserId());
            baseUdafParamService.save(baseUdafParam);
            AggregateFunctionParamBaseUdafParamRelation relation = new AggregateFunctionParamBaseUdafParamRelation();
            relation.setAggregateFunctionParamId(aggregateFunctionParam.getId());
            relation.setUserId(aggregateFunctionParam.getUserId());
            relation.setBaseUdafParamId(baseUdafParam.getId());
            aggregateFunctionParamBaseUdafParamRelationService.save(relation);
        }
        return true;
    }
}