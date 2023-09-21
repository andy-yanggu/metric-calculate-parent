package com.yanggu.metric_calculate.config.service.impl;

import com.googlecode.aviator.Expression;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.AviatorExpressParamMapper;
import com.yanggu.metric_calculate.config.mapstruct.AviatorExpressParamMapstruct;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamAviatorFunctionInstanceRelationService;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamMixUdafParamItemRelationService;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamModelColumnRelationService;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.util.AviatorExpressUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.enums.ResultCode.AVIATOR_EXPRESS_CHECK_ERROR;
import static com.yanggu.metric_calculate.config.enums.ResultCode.AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_ERROR;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AviatorExpressParamAviatorFunctionInstanceRelationTableDef.AVIATOR_EXPRESS_PARAM_AVIATOR_FUNCTION_INSTANCE_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AviatorExpressParamMixUdafParamItemRelationTableDef.AVIATOR_EXPRESS_PARAM_MIX_UDAF_PARAM_ITEM_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AviatorExpressParamModelColumnRelationTableDef.AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_RELATION;

/**
 * Aviator表达式配置 服务层实现。
 */
@Service
public class AviatorExpressParamServiceImpl extends ServiceImpl<AviatorExpressParamMapper, AviatorExpressParam> implements AviatorExpressParamService {

    @Autowired
    private AviatorExpressParamModelColumnRelationService aviatorExpressParamModelColumnRelationService;

    @Autowired
    private AviatorExpressParamAviatorFunctionInstanceRelationService aviatorExpressParamAviatorFunctionInstanceRelationService;

    @Autowired
    private AviatorExpressParamMapstruct aviatorExpressParamMapstruct;

    @Autowired
    private AviatorExpressParamMixUdafParamItemRelationService aviatorExpressParamMixUdafParamItemRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveDataByModelColumn(AviatorExpressParam aviatorExpressParam) throws Exception {
        //校验Aviator表达式
        List<ModelColumn> modelColumnList = aviatorExpressParam.getModelColumnList();
        Set<String> fieldSet;
        if (CollUtil.isEmpty(modelColumnList)) {
            fieldSet = Collections.emptySet();
        } else {
            fieldSet = modelColumnList.stream()
                    .map(ModelColumn::getName)
                    .collect(Collectors.toSet());
        }
        checkAviatorExpress(aviatorExpressParam, fieldSet);
        //保存Aviator表达式
        super.save(aviatorExpressParam);

        //保存Aviator表达式依赖的宽表字段
        if (CollUtil.isNotEmpty(modelColumnList)) {
            List<AviatorExpressParamModelColumnRelation> relationList = modelColumnList.stream()
                    .map(modelColumn -> {
                        AviatorExpressParamModelColumnRelation relation = new AviatorExpressParamModelColumnRelation();
                        relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                        relation.setModelColumnId(modelColumn.getId());
                        return relation;
                    })
                    .toList();
            aviatorExpressParamModelColumnRelationService.saveBatch(relationList);
        }

        //保存Aviator表达式依赖的Aviator函数实例
        saveAviatorFunctionInstanceRelation(aviatorExpressParam);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveDataByMixUdafParamItem(AviatorExpressParam aviatorExpressParam) throws Exception {
        //校验Aviator表达式
        List<MixUdafParamItem> mixUdafParamItemList = aviatorExpressParam.getMixUdafParamItemList();
        Set<String> fieldSet = mixUdafParamItemList.stream().map(MixUdafParamItem::getName).collect(Collectors.toSet());
        checkAviatorExpress(aviatorExpressParam, fieldSet);
        //保存Aviator表达式
        super.save(aviatorExpressParam);

        //保存Aviator表达式依赖的混合参数字段
        if (CollUtil.isNotEmpty(mixUdafParamItemList)) {
            List<AviatorExpressParamMixUdafParamItemRelation> relationList = mixUdafParamItemList.stream()
                    .map(mixUdafParamItem -> {
                        AviatorExpressParamMixUdafParamItemRelation relation = new AviatorExpressParamMixUdafParamItemRelation();
                        relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                        relation.setMixUdafParamItemId(mixUdafParamItem.getId());
                        return relation;
                    })
                    .collect(Collectors.toList());
            aviatorExpressParamMixUdafParamItemRelationService.saveBatch(relationList);
        }

        //保存Aviator表达式依赖的Aviator函数实例
        saveAviatorFunctionInstanceRelation(aviatorExpressParam);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(AviatorExpressParam aviatorExpressParam) {
        Integer aviatorExpressParamId = aviatorExpressParam.getId();
        super.removeById(aviatorExpressParamId);
        List<ModelColumn> modelColumnList = aviatorExpressParam.getModelColumnList();
        //删除和ModelColumn的关联关系
        if (CollUtil.isNotEmpty(modelColumnList)) {
            List<Integer> list = modelColumnList.stream()
                    .map(ModelColumn::getId)
                    .toList();
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(aviatorExpressParamId))
                    .and(AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_RELATION.MODEL_COLUMN_ID.in(list));
            aviatorExpressParamModelColumnRelationService.remove(queryWrapper);
        }
        //删除和MixUdafParamItem之间的关联关系
        List<MixUdafParamItem> mixUdafParamItemList = aviatorExpressParam.getMixUdafParamItemList();
        if (CollUtil.isNotEmpty(mixUdafParamItemList)) {
            List<Integer> list = mixUdafParamItemList.stream()
                    .map(MixUdafParamItem::getId)
                    .toList();
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(AVIATOR_EXPRESS_PARAM_MIX_UDAF_PARAM_ITEM_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(aviatorExpressParamId))
                    .and(AVIATOR_EXPRESS_PARAM_MIX_UDAF_PARAM_ITEM_RELATION.MIX_UDAF_PARAM_ITEM_ID.in(list));
            aviatorExpressParamMixUdafParamItemRelationService.remove(queryWrapper);
        }
        //删除和AviatorFunctionInstance的关联关系
        List<AviatorFunctionInstance> aviatorFunctionInstanceList = aviatorExpressParam.getAviatorFunctionInstanceList();
        if (CollUtil.isNotEmpty(aviatorFunctionInstanceList)) {
            List<Integer> list = aviatorFunctionInstanceList.stream()
                    .map(AviatorFunctionInstance::getId)
                    .toList();
            QueryWrapper queryWrapper = QueryWrapper.create().where(AVIATOR_EXPRESS_PARAM_AVIATOR_FUNCTION_INSTANCE_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(aviatorExpressParamId))
                    .and(AVIATOR_EXPRESS_PARAM_AVIATOR_FUNCTION_INSTANCE_RELATION.AVIATOR_FUNCTION_INSTANCE_ID.in(list));
            aviatorExpressParamAviatorFunctionInstanceRelationService.remove(queryWrapper);
        }
    }

    private void checkAviatorExpress(AviatorExpressParam aviatorExpressParam,
                                     Set<String> fieldSet) throws Exception {
        if (aviatorExpressParam == null) {
            throw new BusinessException(AVIATOR_EXPRESS_CHECK_ERROR);
        }
        String express = aviatorExpressParam.getExpress();
        if (StrUtil.isBlank(express)) {
            throw new BusinessException(AVIATOR_EXPRESS_CHECK_ERROR);
        }

        com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam expressParam =
                                            aviatorExpressParamMapstruct.toCoreAviatorExpressParam(aviatorExpressParam);

        AviatorFunctionFactory aviatorFunctionFactory = new AviatorFunctionFactory();
        aviatorFunctionFactory.init();
        Expression expression = AviatorExpressUtil.compileExpress(expressParam, aviatorFunctionFactory);
        AviatorExpressUtil.checkVariable(expression, fieldSet);
        List<String> variableNames = expression.getVariableNames();
        if (CollUtil.isEmpty(variableNames)) {
            return;
        }
        if (fieldSet.size() != variableNames.size()) {
            throw new BusinessException(AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_ERROR);
        }
    }

    private void saveAviatorFunctionInstanceRelation(AviatorExpressParam aviatorExpressParam) {
        if (aviatorExpressParam == null) {
            return;
        }
        //保存Aviator表达式依赖的Aviator函数实例
        List<AviatorFunctionInstance> aviatorFunctionInstanceList = aviatorExpressParam.getAviatorFunctionInstanceList();
        if (CollUtil.isEmpty(aviatorFunctionInstanceList)) {
            return;
        }
        List<AviatorExpressParamAviatorFunctionInstanceRelation> relationList = aviatorFunctionInstanceList.stream()
                .map(aviatorFunctionInstance -> {
                    AviatorExpressParamAviatorFunctionInstanceRelation relation = new AviatorExpressParamAviatorFunctionInstanceRelation();
                    relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                    relation.setAviatorFunctionInstanceId(aviatorFunctionInstance.getId());
                    return relation;
                })
                .collect(Collectors.toList());
        aviatorExpressParamAviatorFunctionInstanceRelationService.saveBatch(relationList);
    }

}