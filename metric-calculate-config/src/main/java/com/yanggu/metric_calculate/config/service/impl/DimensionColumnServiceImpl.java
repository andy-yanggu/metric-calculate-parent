package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.ModelDimensionColumnMapper;
import com.yanggu.metric_calculate.config.pojo.entity.ModelDimensionColumnEntity;
import com.yanggu.metric_calculate.config.service.ModelDimensionColumnService;
import org.springframework.stereotype.Service;

/**
 * 维度字段 服务层实现。
 */
@Service
public class DimensionColumnServiceImpl extends ServiceImpl<ModelDimensionColumnMapper, ModelDimensionColumnEntity> implements ModelDimensionColumnService {
}