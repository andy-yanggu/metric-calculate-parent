package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.MixUdafParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParam;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamItem;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamMetricExpressRelation;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.MixUdafParamItemService;
import com.yanggu.metric_calculate.config.service.MixUdafParamMetricExpressRelationService;
import com.yanggu.metric_calculate.config.service.MixUdafParamService;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.enums.ResultCode.MIX_UDAF_PARAM_NAME_ERROR;
import static com.yanggu.metric_calculate.config.pojo.entity.table.MixUdafParamMetricExpressRelationTableDef.MIX_UDAF_PARAM_METRIC_EXPRESS_RELATION;

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
        List<MixUdafParamItem> metricExpressParamMixUdafParamItemList = metricExpressParam.getMixUdafParamItemList();
        if (CollUtil.isNotEmpty(metricExpressParamMixUdafParamItemList)) {
            Map<String, MixUdafParamItem> collect = mixUdafParamItemList.stream()
                    .collect(Collectors.toMap(MixUdafParamItem::getName, Function.identity()));
            List<MixUdafParamItem> newList = new ArrayList<>();
            for (MixUdafParamItem mixUdafParamItem : metricExpressParamMixUdafParamItemList) {
                if (collect.get(mixUdafParamItem.getName()) == null) {
                    throw new BusinessException(MIX_UDAF_PARAM_NAME_ERROR);
                }
                newList.add(collect.get(mixUdafParamItem.getName()));
            }
            metricExpressParam.setMixUdafParamItemList(newList);
        }
        aviatorExpressParamService.saveDataByMixUdafParamItem(metricExpressParam);
        MixUdafParamMetricExpressRelation relation = new MixUdafParamMetricExpressRelation();
        relation.setMixUdafParamId(mixUdafParam.getId());
        relation.setAviatorExpressParamId(metricExpressParam.getId());
        mixUdafParamMetricExpressRelationService.save(relation);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(MixUdafParam mixUdafParam) {
        Integer id = mixUdafParam.getId();
        super.removeById(id);
        List<MixUdafParamItem> mixUdafParamItemList = mixUdafParam.getMixUdafParamItemList();
        if (CollUtil.isNotEmpty(mixUdafParamItemList)) {
            for (MixUdafParamItem mixUdafParamItem : mixUdafParamItemList) {
                mixUdafParamItemService.deleteData(mixUdafParamItem);
            }
        }
        AviatorExpressParam metricExpressParam = mixUdafParam.getMetricExpressParam();
        aviatorExpressParamService.deleteData(metricExpressParam);
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(MIX_UDAF_PARAM_METRIC_EXPRESS_RELATION.MIX_UDAF_PARAM_ID.eq(id))
                .and(MIX_UDAF_PARAM_METRIC_EXPRESS_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(metricExpressParam.getId()));
        mixUdafParamMetricExpressRelationService.remove(queryWrapper);
    }

}