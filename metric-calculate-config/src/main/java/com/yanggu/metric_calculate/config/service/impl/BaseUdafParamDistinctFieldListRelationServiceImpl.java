package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.entity.BaseUdafParamDistinctFieldListRelation;
import com.yanggu.metric_calculate.config.mapper.BaseUdafParamDistinctFieldListRelationMapper;
import com.yanggu.metric_calculate.config.service.BaseUdafParamDistinctFieldListRelationService;
import org.springframework.stereotype.Service;

/**
 * 基本聚合参数，去重字段列表中间表 服务层实现。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@Service
public class BaseUdafParamDistinctFieldListRelationServiceImpl extends ServiceImpl<BaseUdafParamDistinctFieldListRelationMapper, BaseUdafParamDistinctFieldListRelation> implements BaseUdafParamDistinctFieldListRelationService {

}