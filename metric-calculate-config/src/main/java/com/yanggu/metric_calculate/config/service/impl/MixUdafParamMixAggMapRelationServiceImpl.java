package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.MixUdafParamMixAggMapRelationMapper;
import com.yanggu.metric_calculate.config.service.MixUdafParamMixAggMapRelationService;
import org.springframework.stereotype.Service;

/**
 * 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-07
 */
@Service
public class MixUdafParamMixAggMapRelationServiceImpl extends ServiceImpl<MixUdafParamMixAggMapRelationMapper, MixUdafParamMixAggMapRelation> implements MixUdafParamMixAggMapRelationService {

}