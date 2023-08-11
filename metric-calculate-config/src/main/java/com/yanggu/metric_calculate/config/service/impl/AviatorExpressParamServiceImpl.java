package com.yanggu.metric_calculate.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.googlecode.aviator.Expression;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.AviatorExpressParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.pojo.exception.BusinessException;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamAviatorFunctionInstanceRelationService;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamModelColumnRelationService;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core2.util.ExpressionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.enums.ResultCode.AVIATOR_EXPRESS_CHECK_ERROR;
import static com.yanggu.metric_calculate.config.enums.ResultCode.AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_ERROR;

/**
 * Aviator表达式配置 服务层实现。
 */
@Service
public class AviatorExpressParamServiceImpl extends ServiceImpl<AviatorExpressParamMapper, AviatorExpressParam> implements AviatorExpressParamService {

    @Autowired
    private AviatorExpressParamMapper aviatorExpressParamMapper;

    @Autowired
    private AviatorExpressParamModelColumnRelationService aviatorExpressParamModelColumnRelationService;

    @Autowired
    private AviatorExpressParamAviatorFunctionInstanceRelationService aviatorExpressParamAviatorFunctionInstanceRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(AviatorExpressParam aviatorExpressParam) throws Exception {
        //校验Aviator表达式
        boolean checkResult = checkAviatorExpress(aviatorExpressParam);
        if (!checkResult) {
            throw new BusinessException(AVIATOR_EXPRESS_CHECK_ERROR);
        }
        //保存Aviator表达式
        aviatorExpressParamMapper.insertSelective(aviatorExpressParam);

        //保存Aviator表达式依赖的宽表字段
        List<ModelColumn> modelColumnList = aviatorExpressParam.getModelColumnList();
        if (CollUtil.isNotEmpty(modelColumnList)) {
            List<AviatorExpressParamModelColumnRelation> relationList = modelColumnList.stream()
                    .map(modelColumn -> {
                        AviatorExpressParamModelColumnRelation relation = new AviatorExpressParamModelColumnRelation();
                        relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                        relation.setModelColumnId(modelColumn.getId());
                        relation.setUserId(aviatorExpressParam.getUserId());
                        return relation;
                    })
                    .collect(Collectors.toList());
            aviatorExpressParamModelColumnRelationService.saveBatch(relationList);
        }

        //保存Aviator表达式依赖的Aviator函数实例
        List<AviatorFunctionInstance> aviatorFunctionInstanceList = aviatorExpressParam.getAviatorFunctionInstanceList();
        if (CollUtil.isNotEmpty(aviatorFunctionInstanceList)) {
            List<AviatorExpressParamAviatorFunctionInstanceRelation> relationList = aviatorFunctionInstanceList.stream()
                    .map(aviatorFunctionInstance -> {
                        AviatorExpressParamAviatorFunctionInstanceRelation relation = new AviatorExpressParamAviatorFunctionInstanceRelation();
                        relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                        relation.setAviatorFunctionInstanceId(aviatorFunctionInstance.getId());
                        relation.setUserId(aviatorExpressParam.getUserId());
                        return relation;
                    })
                    .collect(Collectors.toList());
            aviatorExpressParamAviatorFunctionInstanceRelationService.saveBatch(relationList);
        }
    }

    @Override
    public boolean checkAviatorExpress(AviatorExpressParam aviatorExpressParam) throws Exception {
        if (aviatorExpressParam == null) {
            return false;
        }
        String express = aviatorExpressParam.getExpress();
        if (StrUtil.isBlank(express)) {
            return false;
        }

        com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam expressParam = new com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam();
        expressParam.setExpress(express);

        List<AviatorFunctionInstance> aviatorFunctionInstanceList = aviatorExpressParam.getAviatorFunctionInstanceList();
        if (CollUtil.isNotEmpty(aviatorFunctionInstanceList)) {
            List<com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorFunctionInstance> aviatorFunctionInstanceList2 = new ArrayList<>();
            for (AviatorFunctionInstance aviatorFunctionInstance : aviatorFunctionInstanceList) {
                String param = aviatorFunctionInstance.getParam();
                String name = aviatorFunctionInstance.getAviatorFunction().getName();
                com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorFunctionInstance aviatorFunctionInstance1 = new com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorFunctionInstance();
                aviatorFunctionInstance1.setName(name);
                if (StrUtil.isNotBlank(param)) {
                    aviatorFunctionInstance1.setParam(JSONUtil.parseObj(param));
                }
                aviatorFunctionInstanceList2.add(aviatorFunctionInstance1);
            }
            expressParam.setAviatorFunctionInstanceList(aviatorFunctionInstanceList2);
        }
        AviatorFunctionFactory aviatorFunctionFactory = new AviatorFunctionFactory();
        aviatorFunctionFactory.init();
        Expression expression = ExpressionUtil.compileExpress(expressParam, aviatorFunctionFactory);

        List<ModelColumn> modelColumnList = aviatorExpressParam.getModelColumnList();
        Map<String, Class<?>> fieldMap = new HashMap<>();
        for (ModelColumn modelColumn : modelColumnList) {
            fieldMap.put(modelColumn.getName(), modelColumn.getDataType().getType());
        }
        ExpressionUtil.checkVariable(expression, fieldMap);
        List<String> variableNames = expression.getVariableNames();
        if (fieldMap.size() != variableNames.size()) {
            throw new BusinessException(AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_ERROR);
        }
        return true;
    }

}