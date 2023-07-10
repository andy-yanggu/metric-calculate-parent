package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionColumnItem;
import com.yanggu.metric_calculate.config.mapper.DimensionColumnItemMapper;
import com.yanggu.metric_calculate.config.service.DimensionColumnItemService;
import org.springframework.stereotype.Service;

/**
 * 维度字段选项 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class DimensionColumnItemServiceImpl extends ServiceImpl<DimensionColumnItemMapper, DimensionColumnItem> implements DimensionColumnItemService {

}