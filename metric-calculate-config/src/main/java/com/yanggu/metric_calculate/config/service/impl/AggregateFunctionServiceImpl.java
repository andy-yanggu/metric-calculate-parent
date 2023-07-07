package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.entity.AggregateFunction;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionMapper;
import com.yanggu.metric_calculate.config.service.AggregateFunctionService;
import org.springframework.stereotype.Service;

/**
 * 聚合函数 服务层实现。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@Service
public class AggregateFunctionServiceImpl extends ServiceImpl<AggregateFunctionMapper, AggregateFunction> implements AggregateFunctionService {

}