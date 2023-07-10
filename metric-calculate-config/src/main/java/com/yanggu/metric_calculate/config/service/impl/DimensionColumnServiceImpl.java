package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.entity.DimensionColumn;
import com.yanggu.metric_calculate.config.mapper.DimensionCloumnMapper;
import com.yanggu.metric_calculate.config.service.DimensionColumnService;
import org.springframework.stereotype.Service;

/**
 * 维度字段 服务层实现。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@Service
public class DimensionColumnServiceImpl extends ServiceImpl<DimensionCloumnMapper, DimensionColumn> implements DimensionColumnService {

}