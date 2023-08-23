package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.MixUdafParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParam;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamMetricExpressRelation;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamItem;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.MixUdafParamMetricExpressRelationService;
import com.yanggu.metric_calculate.config.service.MixUdafParamItemService;
import com.yanggu.metric_calculate.config.service.MixUdafParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 混合类型udaf参数 服务层实现。
 */
@Service
public class MixUdafParamServiceImpl extends ServiceImpl<MixUdafParamMapper, MixUdafParam> implements MixUdafParamService {

    @Autowired
    private MixUdafParamItemService mixUdafParamItemService;

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private MixUdafParamMetricExpressRelationService mixUdafParamMetricExpressRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(MixUdafParam mixUdafParam) throws Exception {
        super.save(mixUdafParam);
        List<MixUdafParamItem> mixUdafParamItemList = mixUdafParam.getMixUdafParamItemList();
        for (MixUdafParamItem mixUdafParamItem : mixUdafParamItemList) {
            mixUdafParamItem.setMixUdafParamId(mixUdafParam.getId());
            mixUdafParamItemService.saveData(mixUdafParamItem);
        }

        AviatorExpressParam metricExpressParam = mixUdafParam.getMetricExpressParam();
        aviatorExpressParamService.saveDataByMixUdafParamItem(metricExpressParam);
        MixUdafParamMetricExpressRelation relation = new MixUdafParamMetricExpressRelation();
        relation.setMixUdafParamId(mixUdafParam.getId());
        relation.setAviatorExpressParamId(metricExpressParam.getId());
        relation.setUserId(mixUdafParam.getUserId());
        mixUdafParamMetricExpressRelationService.save(relation);
    }

}