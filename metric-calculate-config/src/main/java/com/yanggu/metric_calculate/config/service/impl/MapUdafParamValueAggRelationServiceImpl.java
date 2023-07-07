package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.MapUdafParamValueAggRelationMapper;
import com.yanggu.metric_calculate.config.service.MapUdafParamValueAggRelationService;
import org.springframework.stereotype.Service;

/**
 * 映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-07
 */
@Service
public class MapUdafParamValueAggRelationServiceImpl extends ServiceImpl<MapUdafParamValueAggRelationMapper, MapUdafParamValueAggRelation> implements MapUdafParamValueAggRelationService {

}