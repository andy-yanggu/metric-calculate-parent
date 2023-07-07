package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.entity.MixUdafParam;
import com.yanggu.metric_calculate.config.mapper.MixUdafParamMapper;
import com.yanggu.metric_calculate.config.service.MixUdafParamService;
import org.springframework.stereotype.Service;

/**
 * 混合类型udaf参数 服务层实现。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@Service
public class MixUdafParamServiceImpl extends ServiceImpl<MixUdafParamMapper, MixUdafParam> implements MixUdafParamService {

}