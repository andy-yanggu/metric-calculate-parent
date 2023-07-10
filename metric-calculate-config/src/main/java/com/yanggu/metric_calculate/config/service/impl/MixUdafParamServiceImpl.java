package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParam;
import com.yanggu.metric_calculate.config.mapper.MixUdafParamMapper;
import com.yanggu.metric_calculate.config.service.MixUdafParamService;
import org.springframework.stereotype.Service;

/**
 * 混合类型udaf参数 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class MixUdafParamServiceImpl extends ServiceImpl<MixUdafParamMapper, MixUdafParam> implements MixUdafParamService {

}