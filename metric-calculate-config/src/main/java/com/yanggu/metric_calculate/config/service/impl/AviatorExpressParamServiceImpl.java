package com.yanggu.metric_calculate.config.service.impl;

import com.googlecode.aviator.Expression;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.domain.entity.*;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.AviatorExpressParamMapper;
import com.yanggu.metric_calculate.config.mapstruct.AviatorExpressParamMapstruct;
import com.yanggu.metric_calculate.config.service.*;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.util.AviatorExpressUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.domain.entity.table.AviatorExpressParamAviatorFunctionInstanceRelationTableDef.AVIATOR_EXPRESS_PARAM_AVIATOR_FUNCTION_INSTANCE_RELATION;
import static com.yanggu.metric_calculate.config.domain.entity.table.AviatorExpressParamMixUdafParamItemRelationTableDef.AVIATOR_EXPRESS_PARAM_MIX_UDAF_PARAM_ITEM_RELATION;
import static com.yanggu.metric_calculate.config.domain.entity.table.AviatorExpressParamModelColumnRelationTableDef.AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_RELATION;
import static com.yanggu.metric_calculate.config.domain.entity.table.AviatorFunctionTableDef.AVIATOR_FUNCTION;
import static com.yanggu.metric_calculate.config.enums.ResultCode.AVIATOR_EXPRESS_CHECK_ERROR;

/**
 * Aviator表达式配置 服务层实现。
 */
@Service
public class AviatorExpressParamServiceImpl extends ServiceImpl<AviatorExpressParamMapper, AviatorExpressParamEntity> implements AviatorExpressParamService {

    @Autowired
    private AviatorExpressParamModelColumnRelationService aviatorExpressParamModelColumnRelationService;

    @Autowired
    private AviatorExpressParamAviatorFunctionInstanceRelationService aviatorExpressParamAviatorFunctionInstanceRelationService;

    @Autowired
    private AviatorExpressParamMapstruct aviatorExpressParamMapstruct;

    @Autowired
    private AviatorExpressParamMixUdafParamItemRelationService aviatorExpressParamMixUdafParamItemRelationService;

    @Autowired
    private AviatorFunctionService aviatorFunctionService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveDataByModelColumn(AviatorExpressParamEntity aviatorExpressParam) throws Exception {
        //校验Aviator表达式
        List<ModelColumnEntity> modelColumnList = aviatorExpressParam.getModelColumnList();
        Set<String> fieldSet;
        if (CollUtil.isEmpty(modelColumnList)) {
            fieldSet = Collections.emptySet();
        } else {
            fieldSet = modelColumnList.stream()
                    .map(ModelColumnEntity::getName)
                    .collect(Collectors.toSet());
        }
        List<String> paramList = checkAviatorExpress(aviatorExpressParam, fieldSet);
        //保存Aviator表达式
        super.save(aviatorExpressParam);

        //保存Aviator表达式依赖的宽表字段
        if (CollUtil.isNotEmpty(modelColumnList)) {
            List<AviatorExpressParamModelColumnRelationEntity> relationList = modelColumnList.stream()
                    //过滤出依赖的宽表字段
                    .filter(modelColumn -> paramList.contains(modelColumn.getName()))
                    .map(modelColumn -> {
                        AviatorExpressParamModelColumnRelationEntity relation = new AviatorExpressParamModelColumnRelationEntity();
                        relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                        relation.setModelColumnId(modelColumn.getId());
                        return relation;
                    })
                    .toList();
            if (CollUtil.isNotEmpty(relationList)) {
                aviatorExpressParamModelColumnRelationService.saveBatch(relationList);
            }
        }

        //保存Aviator表达式依赖的Aviator函数实例
        saveAviatorFunctionInstanceRelation(aviatorExpressParam);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveDataByMixUdafParamItem(AviatorExpressParamEntity aviatorExpressParam) throws Exception {
        //校验Aviator表达式
        List<MixUdafParamItemEntity> mixUdafParamItemList = aviatorExpressParam.getMixUdafParamItemList();
        Set<String> fieldSet = mixUdafParamItemList.stream()
                .map(MixUdafParamItemEntity::getName)
                .collect(Collectors.toSet());
        List<String> paramList = checkAviatorExpress(aviatorExpressParam, fieldSet);
        //保存Aviator表达式
        super.save(aviatorExpressParam);

        //保存Aviator表达式依赖的混合参数字段
        if (CollUtil.isNotEmpty(mixUdafParamItemList)) {
            List<AviatorExpressParamMixUdafParamItemRelationEntity> relationList = mixUdafParamItemList.stream()
                    .filter(mixUdafParamItem -> paramList.contains(mixUdafParamItem.getName()))
                    .map(mixUdafParamItem -> {
                        AviatorExpressParamMixUdafParamItemRelationEntity relation = new AviatorExpressParamMixUdafParamItemRelationEntity();
                        relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                        relation.setMixUdafParamItemId(mixUdafParamItem.getId());
                        return relation;
                    })
                    .toList();
            if (CollUtil.isNotEmpty(relationList)) {
                aviatorExpressParamMixUdafParamItemRelationService.saveBatch(relationList);
            }
        }

        //保存Aviator表达式依赖的Aviator函数实例
        saveAviatorFunctionInstanceRelation(aviatorExpressParam);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(AviatorExpressParamEntity aviatorExpressParam) {
        Integer aviatorExpressParamId = aviatorExpressParam.getId();
        super.removeById(aviatorExpressParamId);
        List<ModelColumnEntity> modelColumnList = aviatorExpressParam.getModelColumnList();
        //删除和ModelColumn的关联关系
        if (CollUtil.isNotEmpty(modelColumnList)) {
            List<Integer> list = modelColumnList.stream()
                    .map(ModelColumnEntity::getId)
                    .toList();
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(aviatorExpressParamId))
                    .and(AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_RELATION.MODEL_COLUMN_ID.in(list));
            aviatorExpressParamModelColumnRelationService.remove(queryWrapper);
        }
        //删除和MixUdafParamItem之间的关联关系
        List<MixUdafParamItemEntity> mixUdafParamItemList = aviatorExpressParam.getMixUdafParamItemList();
        if (CollUtil.isNotEmpty(mixUdafParamItemList)) {
            List<Integer> list = mixUdafParamItemList.stream()
                    .map(MixUdafParamItemEntity::getId)
                    .toList();
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(AVIATOR_EXPRESS_PARAM_MIX_UDAF_PARAM_ITEM_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(aviatorExpressParamId))
                    .and(AVIATOR_EXPRESS_PARAM_MIX_UDAF_PARAM_ITEM_RELATION.MIX_UDAF_PARAM_ITEM_ID.in(list));
            aviatorExpressParamMixUdafParamItemRelationService.remove(queryWrapper);
        }
        //删除和AviatorFunctionInstance的关联关系
        List<AviatorFunctionInstanceEntity> aviatorFunctionInstanceList = aviatorExpressParam.getAviatorFunctionInstanceList();
        if (CollUtil.isNotEmpty(aviatorFunctionInstanceList)) {
            List<Integer> list = aviatorFunctionInstanceList.stream()
                    .map(AviatorFunctionInstanceEntity::getId)
                    .toList();
            QueryWrapper queryWrapper = QueryWrapper.create().where(AVIATOR_EXPRESS_PARAM_AVIATOR_FUNCTION_INSTANCE_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(aviatorExpressParamId))
                    .and(AVIATOR_EXPRESS_PARAM_AVIATOR_FUNCTION_INSTANCE_RELATION.AVIATOR_FUNCTION_INSTANCE_ID.in(list));
            aviatorExpressParamAviatorFunctionInstanceRelationService.remove(queryWrapper);
        }
    }

    private List<String> checkAviatorExpress(AviatorExpressParamEntity aviatorExpressParam,
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
        //如果使用了自定义Aviator函数, 添加jar包路径
        if (CollUtil.isNotEmpty(aviatorExpressParam.getAviatorFunctionInstanceList())) {
            List<Integer> list = aviatorExpressParam.getAviatorFunctionInstanceList().stream()
                    .map(AviatorFunctionInstanceEntity::getAviatorFunctionId)
                    .distinct()
                    .toList();
            List<String> jarList = aviatorFunctionService.queryChain()
                    .where(AVIATOR_FUNCTION.ID.in(list))
                    .withRelations()
                    .list()
                    .stream()
                    .map(AviatorFunctionEntity::getJarStore)
                    .filter(Objects::nonNull)
                    .map(JarStoreEntity::getJarUrl)
                    .distinct()
                    .toList();
            aviatorFunctionFactory.setUdfJarPathList(jarList);
        }
        aviatorFunctionFactory.init();
        Expression expression = AviatorExpressUtil.compileExpress(expressParam, aviatorFunctionFactory);
        AviatorExpressUtil.checkVariable(expression, fieldSet);
        return expression.getVariableNames();
    }

    private void saveAviatorFunctionInstanceRelation(AviatorExpressParamEntity aviatorExpressParam) {
        if (aviatorExpressParam == null) {
            return;
        }
        //保存Aviator表达式依赖的Aviator函数实例
        List<AviatorFunctionInstanceEntity> aviatorFunctionInstanceList = aviatorExpressParam.getAviatorFunctionInstanceList();
        if (CollUtil.isEmpty(aviatorFunctionInstanceList)) {
            return;
        }
        List<AviatorExpressParamAviatorFunctionInstanceRelationEntity> relationList = aviatorFunctionInstanceList.stream()
                .map(aviatorFunctionInstance -> {
                    AviatorExpressParamAviatorFunctionInstanceRelationEntity relation = new AviatorExpressParamAviatorFunctionInstanceRelationEntity();
                    relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                    relation.setAviatorFunctionInstanceId(aviatorFunctionInstance.getId());
                    return relation;
                })
                .toList();
        aviatorExpressParamAviatorFunctionInstanceRelationService.saveBatch(relationList);
    }

}