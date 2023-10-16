package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.BaseUdafParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.BaseUdafParamMetricExpressListRelationService;
import com.yanggu.metric_calculate.config.service.BaseUdafParamMetricExpressRelationService;
import com.yanggu.metric_calculate.config.service.BaseUdafParamService;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.pojo.entity.table.BaseUdafParamMetricExpressListRelationTableDef.BASE_UDAF_PARAM_METRIC_EXPRESS_LIST_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.BaseUdafParamMetricExpressRelationTableDef.BASE_UDAF_PARAM_METRIC_EXPRESS_RELATION;

/**
 * 数值型、集合型、对象型聚合函数相关参数 服务层实现。
 */
@Service
public class BaseUdafParamServiceImpl extends ServiceImpl<BaseUdafParamMapper, BaseUdafParam> implements BaseUdafParamService {

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private BaseUdafParamMetricExpressRelationService metricExpressRelationService;

    @Autowired
    private BaseUdafParamMetricExpressListRelationService metricExpressListRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(BaseUdafParam baseUdafParam, List<ModelColumn> modelColumnList) throws Exception {
        super.save(baseUdafParam);
        AviatorExpressParam metricExpressParam = baseUdafParam.getMetricExpressParam();
        if (metricExpressParam != null) {
            metricExpressParam.setModelColumnList(modelColumnList);
            aviatorExpressParamService.saveDataByModelColumn(metricExpressParam);
            BaseUdafParamMetricExpressRelation relation = new BaseUdafParamMetricExpressRelation();
            relation.setBaseUdafParamId(baseUdafParam.getId());
            relation.setAviatorExpressParamId(metricExpressParam.getId());
            metricExpressRelationService.save(relation);
        }
        List<AviatorExpressParam> metricExpressParamList = baseUdafParam.getMetricExpressParamList();
        if (CollUtil.isNotEmpty(metricExpressParamList)) {
            for (AviatorExpressParam aviatorExpressParam : metricExpressParamList) {
                aviatorExpressParam.setModelColumnList(modelColumnList);
                aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);
                BaseUdafParamMetricExpressListRelation relation = new BaseUdafParamMetricExpressListRelation();
                relation.setBaseUdafParamId(baseUdafParam.getId());
                relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                metricExpressListRelationService.save(relation);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(BaseUdafParam baseUdafParam) {
        Integer baseUdafParamId = baseUdafParam.getId();
        super.removeById(baseUdafParamId);
        AviatorExpressParam metricExpressParam = baseUdafParam.getMetricExpressParam();
        if (metricExpressParam != null) {
            aviatorExpressParamService.deleteData(metricExpressParam);
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(BASE_UDAF_PARAM_METRIC_EXPRESS_RELATION.BASE_UDAF_PARAM_ID.eq(baseUdafParamId))
                    .and(BASE_UDAF_PARAM_METRIC_EXPRESS_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(metricExpressParam.getId()));
            metricExpressRelationService.remove(queryWrapper);
        }

        List<AviatorExpressParam> metricExpressParamList = baseUdafParam.getMetricExpressParamList();
        if (CollUtil.isNotEmpty(metricExpressParamList)) {
            for (AviatorExpressParam aviatorExpressParam : metricExpressParamList) {
                aviatorExpressParamService.deleteData(aviatorExpressParam);
            }
            List<Integer> list = metricExpressParamList.stream().map(AviatorExpressParam::getId).toList();
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(BASE_UDAF_PARAM_METRIC_EXPRESS_LIST_RELATION.BASE_UDAF_PARAM_ID.eq(baseUdafParamId))
                    .and(BASE_UDAF_PARAM_METRIC_EXPRESS_LIST_RELATION.AVIATOR_EXPRESS_PARAM_ID.in(list));
            metricExpressListRelationService.remove(queryWrapper);
        }
    }

}