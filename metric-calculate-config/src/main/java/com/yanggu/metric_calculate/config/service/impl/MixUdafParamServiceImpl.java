package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.MixUdafParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.MixUdafParamItemService;
import com.yanggu.metric_calculate.config.service.MixUdafParamMetricExpressRelationService;
import com.yanggu.metric_calculate.config.service.MixUdafParamService;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.pojo.entity.table.MixUdafParamMetricExpressRelationTableDef.MIX_UDAF_PARAM_METRIC_EXPRESS_RELATION;

/**
 * 混合类型udaf参数 服务层实现。
 */
@Service
public class MixUdafParamServiceImpl extends ServiceImpl<MixUdafParamMapper, MixUdafParamEntity> implements MixUdafParamService {

    @Autowired
    private MixUdafParamItemService mixUdafParamItemService;

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private MixUdafParamMetricExpressRelationService mixUdafParamMetricExpressRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(MixUdafParamEntity mixUdafParam, List<ModelColumnEntity> modelColumnList) throws Exception {
        super.save(mixUdafParam);
        List<MixUdafParamItemEntity> mixUdafParamItemList = mixUdafParam.getMixUdafParamItemList();
        for (MixUdafParamItemEntity mixUdafParamItem : mixUdafParamItemList) {
            mixUdafParamItem.setMixUdafParamId(mixUdafParam.getId());
            mixUdafParamItemService.saveData(mixUdafParamItem, modelColumnList);
        }

        AviatorExpressParamEntity metricExpressParam = mixUdafParam.getMetricExpressParam();
        metricExpressParam.setMixUdafParamItemList(mixUdafParamItemList);
        aviatorExpressParamService.saveDataByMixUdafParamItem(metricExpressParam);
        MixUdafParamMetricExpressRelationEntity relation = new MixUdafParamMetricExpressRelationEntity();
        relation.setMixUdafParamId(mixUdafParam.getId());
        relation.setAviatorExpressParamId(metricExpressParam.getId());
        mixUdafParamMetricExpressRelationService.save(relation);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(MixUdafParamEntity mixUdafParam) {
        Integer id = mixUdafParam.getId();
        super.removeById(id);
        List<MixUdafParamItemEntity> mixUdafParamItemList = mixUdafParam.getMixUdafParamItemList();
        if (CollUtil.isNotEmpty(mixUdafParamItemList)) {
            for (MixUdafParamItemEntity mixUdafParamItem : mixUdafParamItemList) {
                mixUdafParamItemService.deleteData(mixUdafParamItem);
            }
        }
        AviatorExpressParamEntity metricExpressParam = mixUdafParam.getMetricExpressParam();
        aviatorExpressParamService.deleteData(metricExpressParam);
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(MIX_UDAF_PARAM_METRIC_EXPRESS_RELATION.MIX_UDAF_PARAM_ID.eq(id))
                .and(MIX_UDAF_PARAM_METRIC_EXPRESS_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(metricExpressParam.getId()));
        mixUdafParamMetricExpressRelationService.remove(queryWrapper);
    }

}