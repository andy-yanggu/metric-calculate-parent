package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.BaseUdafParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.service.*;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    private BaseUdafParamRetainExpressRelationService retainExpressRelationService;

    @Autowired
    private BaseUdafParamObjectiveCompareFieldExpressListRelationService objectiveCompareFieldExpressListRelationService;

    @Autowired
    private FieldOrderParamService fieldOrderParamService;

    @Autowired
    private BaseUdafParamCollectiveSortFieldListRelationService collectiveSortFieldListRelationService;

    @Autowired
    private BaseUdafParamDistinctFieldListRelationService distinctFieldListRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(BaseUdafParam baseUdafParam) throws Exception {
        super.save(baseUdafParam);
        AviatorExpressParam metricExpressParam = baseUdafParam.getMetricExpressParam();
        if (metricExpressParam != null) {
            metricExpressParam.setUserId(baseUdafParam.getUserId());
            aviatorExpressParamService.saveDataByModelColumn(metricExpressParam);
            BaseUdafParamMetricExpressRelation relation = new BaseUdafParamMetricExpressRelation();
            relation.setBaseUdafParamId(baseUdafParam.getId());
            relation.setAviatorExpressParamId(metricExpressParam.getId());
            relation.setUserId(baseUdafParam.getUserId());
            metricExpressRelationService.save(relation);
        }
        List<AviatorExpressParam> metricExpressParamList = baseUdafParam.getMetricExpressParamList();
        if (CollUtil.isNotEmpty(metricExpressParamList)) {
            for (AviatorExpressParam aviatorExpressParam : metricExpressParamList) {
                aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);
                BaseUdafParamMetricExpressListRelation relation = new BaseUdafParamMetricExpressListRelation();
                relation.setBaseUdafParamId(baseUdafParam.getId());
                relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                relation.setUserId(baseUdafParam.getUserId());
                metricExpressListRelationService.save(relation);
            }
        }
        AviatorExpressParam retainExpressParam = baseUdafParam.getRetainExpressParam();
        if (retainExpressParam != null) {
            aviatorExpressParamService.saveDataByModelColumn(retainExpressParam);
            BaseUdafParamRetainExpressRelation relation = new BaseUdafParamRetainExpressRelation();
            relation.setBaseUdafParamId(baseUdafParam.getId());
            relation.setAviatorExpressParamId(retainExpressParam.getId());
            relation.setUserId(baseUdafParam.getUserId());
            retainExpressRelationService.save(relation);
        }
        List<AviatorExpressParam> objectiveCompareFieldParamList = baseUdafParam.getObjectiveCompareFieldParamList();
        if (CollUtil.isNotEmpty(objectiveCompareFieldParamList)) {
            for (AviatorExpressParam aviatorExpressParam : objectiveCompareFieldParamList) {
                aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);
                BaseUdafParamObjectiveCompareFieldExpressListRelation relation = new BaseUdafParamObjectiveCompareFieldExpressListRelation();
                relation.setBaseUdafParamId(baseUdafParam.getId());
                relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                relation.setUserId(baseUdafParam.getUserId());
                objectiveCompareFieldExpressListRelationService.save(relation);
            }
        }
        List<FieldOrderParam> collectiveSortFieldList = baseUdafParam.getCollectiveSortFieldList();
        if (CollUtil.isNotEmpty(collectiveSortFieldList)) {
            for (FieldOrderParam fieldOrderParam : collectiveSortFieldList) {
                fieldOrderParamService.saveData(fieldOrderParam);
                BaseUdafParamCollectiveSortFieldListRelation relation = new BaseUdafParamCollectiveSortFieldListRelation();
                relation.setBaseUdafParamId(baseUdafParam.getId());
                relation.setFieldOrderParamId(fieldOrderParam.getId());
                relation.setUserId(baseUdafParam.getUserId());
                collectiveSortFieldListRelationService.save(relation);
            }
        }
        List<AviatorExpressParam> distinctFieldListParamList = baseUdafParam.getDistinctFieldListParamList();
        if (CollUtil.isNotEmpty(distinctFieldListParamList)) {
            for (AviatorExpressParam aviatorExpressParam : distinctFieldListParamList) {
                aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);
                BaseUdafParamDistinctFieldListRelation relation = new BaseUdafParamDistinctFieldListRelation();
                relation.setBaseUdafParamId(baseUdafParam.getId());
                relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                relation.setUserId(baseUdafParam.getUserId());
                distinctFieldListRelationService.save(relation);
            }
        }
    }

}