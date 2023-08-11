package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.ModelTimeColumnMapper;
import com.yanggu.metric_calculate.config.pojo.entity.ModelTimeColumn;
import com.yanggu.metric_calculate.config.service.ModelTimeColumnService;
import org.springframework.stereotype.Service;

/**
 * 时间字段 服务层实现。
 */
@Service
public class ModelTimeColumnServiceImpl extends ServiceImpl<ModelTimeColumnMapper, ModelTimeColumn> implements ModelTimeColumnService {
}