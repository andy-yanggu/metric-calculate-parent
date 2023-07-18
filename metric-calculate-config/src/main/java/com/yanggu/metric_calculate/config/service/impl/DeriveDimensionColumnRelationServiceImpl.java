package com.yanggu.metric_calculate.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapstruct.DimensionColumnItemMapstruct;
import com.yanggu.metric_calculate.config.pojo.entity.DeriveDimensionColumnRelation;
import com.yanggu.metric_calculate.config.mapper.DimensionColumnItemMapper;
import com.yanggu.metric_calculate.config.service.DeriveDimensionColumnRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 维度字段选项 服务层实现。
 */
@Service
public class DeriveDimensionColumnRelationServiceImpl extends ServiceImpl<DimensionColumnItemMapper, DeriveDimensionColumnRelation> implements DeriveDimensionColumnRelationService {

    @Autowired
    private DimensionColumnItemMapstruct dimensionColumnItemMapstruct;

    @Autowired
    private DimensionColumnItemMapper dimensionColumnItemMapper;

}