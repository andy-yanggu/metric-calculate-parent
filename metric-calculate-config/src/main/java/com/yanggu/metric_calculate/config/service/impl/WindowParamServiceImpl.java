package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.entity.WindowParam;
import com.yanggu.metric_calculate.config.mapper.WindowParamMapper;
import com.yanggu.metric_calculate.config.service.WindowParamService;
import org.springframework.stereotype.Service;

/**
 * 窗口相关参数 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-07
 */
@Service
public class WindowParamServiceImpl extends ServiceImpl<WindowParamMapper, WindowParam> implements WindowParamService {

}