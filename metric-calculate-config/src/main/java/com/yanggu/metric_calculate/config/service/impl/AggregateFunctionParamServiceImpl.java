package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionParam;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionParamMapper;
import com.yanggu.metric_calculate.config.service.AggregateFunctionParamService;
import org.springframework.stereotype.Service;

/**
 * 聚合函数参数配置类 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class AggregateFunctionParamServiceImpl extends ServiceImpl<AggregateFunctionParamMapper, AggregateFunctionParam> implements AggregateFunctionParamService {

}