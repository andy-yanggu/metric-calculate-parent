package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.entity.MapUdafParamDistinctFieldListRelation;
import com.yanggu.metric_calculate.config.mapper.MapUdafParamDistinctFieldListRelationMapper;
import com.yanggu.metric_calculate.config.service.MapUdafParamDistinctFieldListRelationService;
import org.springframework.stereotype.Service;

/**
 * 映射聚合参数，key的生成逻辑(去重字段列表)中间表 服务层实现。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@Service
public class MapUdafParamDistinctFieldListRelationServiceImpl extends ServiceImpl<MapUdafParamDistinctFieldListRelationMapper, MapUdafParamDistinctFieldListRelation> implements MapUdafParamDistinctFieldListRelationService {

}