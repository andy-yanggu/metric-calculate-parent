package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.domain.entity.AtomAggregateFunctionParamRelationEntity;
import com.yanggu.metric_calculate.config.mapper.AtomAggregateFunctionParamRelationMapper;
import com.yanggu.metric_calculate.config.service.AtomAggregateFunctionParamRelationService;
import org.springframework.stereotype.Service;

/**
 * 原子指标聚合函数参数中间表 服务层实现。
 */
@Service
public class AtomAggregateFunctionParamRelationServiceImpl extends ServiceImpl<AtomAggregateFunctionParamRelationMapper, AtomAggregateFunctionParamRelationEntity> implements AtomAggregateFunctionParamRelationService {
}