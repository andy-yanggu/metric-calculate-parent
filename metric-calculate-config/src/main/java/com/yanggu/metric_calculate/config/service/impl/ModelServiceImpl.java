package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.pojo.entity.Model;
import com.yanggu.metric_calculate.config.mapper.ModelMapper;
import com.yanggu.metric_calculate.config.service.ModelService;
import org.springframework.stereotype.Service;

/**
 * 数据明细宽表 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {

}