package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.domain.entity.MixUdafParamMetricExpressRelationEntity;
import com.yanggu.metric_calculate.config.mapper.MixUdafParamMetricExpressRelationMapper;
import com.yanggu.metric_calculate.config.service.MixUdafParamMetricExpressRelationService;
import org.springframework.stereotype.Service;

/**
 * 混合聚合参数，多个聚合值的计算表达式中间表 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class MixUdafParamMetricExpressRelationServiceImpl extends ServiceImpl<MixUdafParamMetricExpressRelationMapper, MixUdafParamMetricExpressRelationEntity> implements MixUdafParamMetricExpressRelationService {

}
