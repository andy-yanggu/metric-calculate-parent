package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.DeriveModelDimensionColumnRelationMapper;
import com.yanggu.metric_calculate.config.mapstruct.DeriveModelDimensionColumnRelationMapstruct;
import com.yanggu.metric_calculate.config.pojo.entity.DeriveModelDimensionColumnRelation;
import com.yanggu.metric_calculate.config.service.DeriveModelDimensionColumnRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 维度字段选项 服务层实现。
 */
@Service
public class DeriveModelDimensionColumnRelationServiceImpl extends ServiceImpl<DeriveModelDimensionColumnRelationMapper, DeriveModelDimensionColumnRelation> implements DeriveModelDimensionColumnRelationService {

    @Autowired
    private DeriveModelDimensionColumnRelationMapstruct deriveModelDimensionColumnRelationMapstruct;

    @Autowired
    private DeriveModelDimensionColumnRelationMapper deriveModelDimensionColumnRelationMapper;

}