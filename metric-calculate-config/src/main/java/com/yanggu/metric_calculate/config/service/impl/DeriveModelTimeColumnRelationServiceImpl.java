package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.DeriveModelTimeColumnRelationMapper;
import com.yanggu.metric_calculate.config.pojo.entity.DeriveModelTimeColumnRelation;
import com.yanggu.metric_calculate.config.service.DeriveModelTimeColumnRelationService;
import org.springframework.stereotype.Service;

/**
 * 派生指标和时间字段中间表 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class DeriveModelTimeColumnRelationServiceImpl extends ServiceImpl<DeriveModelTimeColumnRelationMapper, DeriveModelTimeColumnRelation> implements DeriveModelTimeColumnRelationService {

}