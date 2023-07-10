package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.pojo.entity.TimeColumn;
import com.yanggu.metric_calculate.config.mapper.TimeColumnMapper;
import com.yanggu.metric_calculate.config.service.TimeColumnService;
import org.springframework.stereotype.Service;

/**
 * 时间字段 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class TimeColumnServiceImpl extends ServiceImpl<TimeColumnMapper, TimeColumn> implements TimeColumnService {

}