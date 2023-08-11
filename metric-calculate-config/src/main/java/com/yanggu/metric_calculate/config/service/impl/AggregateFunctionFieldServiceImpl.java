package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionField;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionFieldMapper;
import com.yanggu.metric_calculate.config.service.AggregateFunctionFieldService;
import org.springframework.stereotype.Service;

/**
 * 聚合函数的字段 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class AggregateFunctionFieldServiceImpl extends ServiceImpl<AggregateFunctionFieldMapper, AggregateFunctionField> implements AggregateFunctionFieldService {

}