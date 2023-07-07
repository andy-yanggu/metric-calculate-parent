package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.entity.TimeCloumn;
import com.yanggu.metric_calculate.config.mapper.TimeCloumnMapper;
import com.yanggu.metric_calculate.config.service.TimeCloumnService;
import org.springframework.stereotype.Service;

/**
 * 时间字段 服务层实现。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@Service
public class TimeCloumnServiceImpl extends ServiceImpl<TimeCloumnMapper, TimeCloumn> implements TimeCloumnService {

}