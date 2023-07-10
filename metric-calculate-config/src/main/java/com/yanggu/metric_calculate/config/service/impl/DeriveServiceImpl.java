package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.entity.Derive;
import com.yanggu.metric_calculate.config.mapper.DeriveMapper;
import com.yanggu.metric_calculate.config.service.DeriveService;
import org.springframework.stereotype.Service;

/**
 * 派生指标 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class DeriveServiceImpl extends ServiceImpl<DeriveMapper, Derive> implements DeriveService {

}