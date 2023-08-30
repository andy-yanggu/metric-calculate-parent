package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.BaseUdafParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.service.*;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.pojo.entity.table.BaseUdafParamDistinctFieldListRelationTableDef.BASE_UDAF_PARAM_DISTINCT_FIELD_LIST_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.BaseUdafParamMetricExpressListRelationTableDef.BASE_UDAF_PARAM_METRIC_EXPRESS_LIST_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.BaseUdafParamMetricExpressRelationTableDef.BASE_UDAF_PARAM_METRIC_EXPRESS_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.BaseUdafParamObjectiveCompareFieldExpressListRelationTableDef.BASE_UDAF_PARAM_OBJECTIVE_COMPARE_FIELD_EXPRESS_LIST_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.BaseUdafParamRetainExpressRelationTableDef.BASE_UDAF_PARAM_RETAIN_EXPRESS_RELATION;

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
            aviatorExpressParamService.saveDataByModelColumn(metricExpressParam);
            BaseUdafParamMetricExpressRelation relation = new BaseUdafParamMetricExpressRelation();
            relation.setBaseUdafParamId(baseUdafParam.getId());
            relation.setAviatorExpressParamId(metricExpressParam.getId());
            metricExpressRelationService.save(relation);
        }
        List<AviatorExpressParam> metricExpressParamList = baseUdafParam.getMetricExpressParamList();
        if (CollUtil.isNotEmpty(metricExpressParamList)) {
            for (AviatorExpressParam aviatorExpressParam : metricExpressParamList) {
                aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);
                BaseUdafParamMetricExpressListRelation relation = new BaseUdafParamMetricExpressListRelation();
                relation.setBaseUdafParamId(baseUdafParam.getId());
                relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                metricExpressListRelationService.save(relation);
            }
        }
        AviatorExpressParam retainExpressParam = baseUdafParam.getRetainExpressParam();
        if (retainExpressParam != null) {
            aviatorExpressParamService.saveDataByModelColumn(retainExpressParam);
            BaseUdafParamRetainExpressRelation relation = new BaseUdafParamRetainExpressRelation();
            relation.setBaseUdafParamId(baseUdafParam.getId());
            relation.setAviatorExpressParamId(retainExpressParam.getId());
            retainExpressRelationService.save(relation);
        }
        List<AviatorExpressParam> objectiveCompareFieldParamList = baseUdafParam.getObjectiveCompareFieldParamList();
        if (CollUtil.isNotEmpty(objectiveCompareFieldParamList)) {
            for (AviatorExpressParam aviatorExpressParam : objectiveCompareFieldParamList) {
                aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);
                BaseUdafParamObjectiveCompareFieldExpressListRelation relation = new BaseUdafParamObjectiveCompareFieldExpressListRelation();
                relation.setBaseUdafParamId(baseUdafParam.getId());
                relation.setAviatorExpressParamId(aviatorExpressParam.getId());
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
                distinctFieldListRelationService.save(relation);
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
        AviatorExpressParam retainExpressParam = baseUdafParam.getRetainExpressParam();
        if (retainExpressParam != null) {
            aviatorExpressParamService.deleteData(retainExpressParam);
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(BASE_UDAF_PARAM_RETAIN_EXPRESS_RELATION.BASE_UDAF_PARAM_ID.eq(baseUdafParamId))
                    .and(BASE_UDAF_PARAM_RETAIN_EXPRESS_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(retainExpressParam.getId()));
            retainExpressRelationService.remove(queryWrapper);
        }

        List<AviatorExpressParam> objectiveCompareFieldParamList = baseUdafParam.getObjectiveCompareFieldParamList();
        if (CollUtil.isNotEmpty(objectiveCompareFieldParamList)) {
            for (AviatorExpressParam aviatorExpressParam : objectiveCompareFieldParamList) {
                aviatorExpressParamService.deleteData(aviatorExpressParam);
            }
            List<Integer> list = objectiveCompareFieldParamList.stream().map(AviatorExpressParam::getId).toList();
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(BASE_UDAF_PARAM_OBJECTIVE_COMPARE_FIELD_EXPRESS_LIST_RELATION.BASE_UDAF_PARAM_ID.eq(baseUdafParamId))
                    .and(BASE_UDAF_PARAM_OBJECTIVE_COMPARE_FIELD_EXPRESS_LIST_RELATION.AVIATOR_EXPRESS_PARAM_ID.in(list));
            objectiveCompareFieldExpressListRelationService.remove(queryWrapper);
        }

        List<FieldOrderParam> collectiveSortFieldList = baseUdafParam.getCollectiveSortFieldList();
        if (CollUtil.isNotEmpty(collectiveSortFieldList)) {
            for (FieldOrderParam fieldOrderParam : collectiveSortFieldList) {
                fieldOrderParamService.deleteData(fieldOrderParam);
            }
        }

        List<AviatorExpressParam> distinctFieldListParamList = baseUdafParam.getDistinctFieldListParamList();
        if (CollUtil.isNotEmpty(distinctFieldListParamList)) {
            for (AviatorExpressParam aviatorExpressParam : distinctFieldListParamList) {
                aviatorExpressParamService.deleteData(aviatorExpressParam);
            }
            List<Integer> list = distinctFieldListParamList.stream().map(AviatorExpressParam::getId).toList();
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(BASE_UDAF_PARAM_DISTINCT_FIELD_LIST_RELATION.BASE_UDAF_PARAM_ID.eq(baseUdafParamId))
                    .and(BASE_UDAF_PARAM_DISTINCT_FIELD_LIST_RELATION.AVIATOR_EXPRESS_PARAM_ID.in(list));
            distinctFieldListRelationService.remove(queryWrapper);
        }
    }

}