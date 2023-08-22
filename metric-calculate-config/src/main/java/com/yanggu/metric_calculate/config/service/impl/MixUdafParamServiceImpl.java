package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.MixUdafParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParam;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamMixAggMapRelation;
import com.yanggu.metric_calculate.config.service.MixUdafParamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 混合类型udaf参数 服务层实现。
 */
@Service
public class MixUdafParamServiceImpl extends ServiceImpl<MixUdafParamMapper, MixUdafParam> implements MixUdafParamService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(MixUdafParam mixUdafParam) {
        super.save(mixUdafParam);
        //TODO 完成MixUdafParamMixAggMapRelation的保存
        List<MixUdafParamMixAggMapRelation> mixUdafParamMixAggMapRelationList = mixUdafParam.getMixUdafParamMixAggMapRelationList();

        //TODO 此表达式不能直接调用saveData方法, 没有依赖的modelColumn
        AviatorExpressParam metricExpressParam = mixUdafParam.getMetricExpressParam();

    }

}