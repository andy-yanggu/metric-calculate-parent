package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.DeriveModelDimensionColumnRelationMapper;
import com.yanggu.metric_calculate.config.domain.entity.DeriveModelDimensionColumnRelationEntity;
import com.yanggu.metric_calculate.config.service.DeriveModelDimensionColumnRelationService;
import org.springframework.stereotype.Service;

/**
 * 维度字段选项 服务层实现。
 */
@Service
public class DeriveModelDimensionColumnRelationServiceImpl extends ServiceImpl<DeriveModelDimensionColumnRelationMapper, DeriveModelDimensionColumnRelationEntity> implements DeriveModelDimensionColumnRelationService {
}