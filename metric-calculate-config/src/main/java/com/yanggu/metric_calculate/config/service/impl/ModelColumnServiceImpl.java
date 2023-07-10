package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import com.yanggu.metric_calculate.config.mapper.ModelColumnMapper;
import com.yanggu.metric_calculate.config.service.ModelColumnService;
import org.springframework.stereotype.Service;

/**
 * 宽表字段 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class ModelColumnServiceImpl extends ServiceImpl<ModelColumnMapper, ModelColumn> implements ModelColumnService {

}