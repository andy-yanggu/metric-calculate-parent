package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.entity.DeriveWindowParamRelation;
import com.yanggu.metric_calculate.config.mapper.DeriveWindowParamRelationMapper;
import com.yanggu.metric_calculate.config.service.DeriveWindowParamRelationService;
import org.springframework.stereotype.Service;

/**
 * 派生指标-窗口参数中间表 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class DeriveWindowParamRelationServiceImpl extends ServiceImpl<DeriveWindowParamRelationMapper, DeriveWindowParamRelation> implements DeriveWindowParamRelationService {

}