package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.entity.Dimension;
import com.yanggu.metric_calculate.config.mapper.DimensionMapper;
import com.yanggu.metric_calculate.config.service.DimensionService;
import org.springframework.stereotype.Service;

/**
 * 维度表 服务层实现。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@Service
public class DimensionServiceImpl extends ServiceImpl<DimensionMapper, Dimension> implements DimensionService {

}