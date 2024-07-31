package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.BaseUdafParamMetricExpressListRelationMapper;
import com.yanggu.metric_calculate.config.domain.entity.BaseUdafParamMetricExpressListRelationEntity;
import com.yanggu.metric_calculate.config.service.BaseUdafParamMetricExpressListRelationService;
import org.springframework.stereotype.Service;

/**
 * 基本聚合参数，多字段度量字段表达式中间表 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class BaseUdafParamMetricExpressListRelationServiceImpl extends ServiceImpl<BaseUdafParamMetricExpressListRelationMapper, BaseUdafParamMetricExpressListRelationEntity> implements BaseUdafParamMetricExpressListRelationService {

}