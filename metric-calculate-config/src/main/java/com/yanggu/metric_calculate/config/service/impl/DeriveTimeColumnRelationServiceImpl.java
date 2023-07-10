package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.entity.DeriveTimeColumnRelation;
import com.yanggu.metric_calculate.config.mapper.DeriveTimeColumnRelationMapper;
import com.yanggu.metric_calculate.config.service.DeriveTimeColumnRelationService;
import org.springframework.stereotype.Service;

/**
 * 派生指标和时间字段中间表 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class DeriveTimeColumnRelationServiceImpl extends ServiceImpl<DeriveTimeColumnRelationMapper, DeriveTimeColumnRelation> implements DeriveTimeColumnRelationService {

}