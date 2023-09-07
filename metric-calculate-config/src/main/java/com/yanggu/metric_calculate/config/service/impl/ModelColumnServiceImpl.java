package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.ModelColumnMapper;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumnAviatorExpressParamRelation;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.ModelColumnAviatorExpressRelationService;
import com.yanggu.metric_calculate.config.service.ModelColumnService;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.enums.ModelColumnFieldType.VIRTUAL;
import static com.yanggu.metric_calculate.config.enums.ResultCode.*;

/**
 * 宽表字段 服务层实现。
 */
@Service
public class ModelColumnServiceImpl extends ServiceImpl<ModelColumnMapper, ModelColumn> implements ModelColumnService {

    @Autowired
    private ModelColumnMapper modelColumnMapper;

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private ModelColumnAviatorExpressRelationService modelColumnAviatorExpressRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveModelColumnList(List<ModelColumn> modelColumnList) throws Exception {
        //校验名称或者中文名是否重复
        checkModelColumList(modelColumnList);
        //批量保存宽表字段
        saveBatch(modelColumnList);
        Map<String, ModelColumn> collect = modelColumnList.stream()
                .collect(Collectors.toMap(ModelColumn::getName, Function.identity()));
        for (ModelColumn modelColumn : modelColumnList) {
            if (!VIRTUAL.equals(modelColumn.getFieldType())) {
                continue;
            }
            //如果是虚拟字段, 保存Aviator表达式和中间表数据
            AviatorExpressParam aviatorExpressParam = modelColumn.getAviatorExpressParam();
            List<ModelColumn> tempModelColumnList = aviatorExpressParam.getModelColumnList();
            if (CollUtil.isNotEmpty(tempModelColumnList)) {
                List<ModelColumn> newTempModelColumnList = new ArrayList<>();
                for (ModelColumn column : tempModelColumnList) {
                    ModelColumn tempModelColumn = collect.get(column.getName());
                    if (tempModelColumn == null) {
                        throw new BusinessException(MODEL_COLUMN_NAME_ERROR);
                    }
                    newTempModelColumnList.add(tempModelColumn);
                }
                aviatorExpressParam.setModelColumnList(newTempModelColumnList);
            }
            aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);

            ModelColumnAviatorExpressParamRelation relation = new ModelColumnAviatorExpressParamRelation();
            relation.setModelColumnId(modelColumn.getId());
            relation.setAviatorExpressParamId(aviatorExpressParam.getId());
            modelColumnAviatorExpressRelationService.save(relation);
        }
    }

    /**
     * 校验名称或者中文名是否重复
     *
     * @param modelColumnList
     */
    private void checkModelColumList(List<ModelColumn> modelColumnList) {
        if (CollUtil.isEmpty(modelColumnList)) {
            throw new BusinessException(MODEL_COLUMN_EMPTY);
        }
        //校验宽表字段名是否重复
        Set<String> nameSet = new HashSet<>();
        Set<String> displayNameSet = new HashSet<>();
        for (ModelColumn modelColumn : modelColumnList) {
            nameSet.add(modelColumn.getName());
            displayNameSet.add(modelColumn.getDisplayName());
        }
        if (nameSet.size() != modelColumnList.size()) {
            throw new BusinessException(MODEL_COLUMN_NAME_DUPLICATE);
        }
        if (displayNameSet.size() != modelColumnList.size()) {
            throw new BusinessException(MODEL_COLUMN_DISPLAY_NAME_DUPLICATE);
        }
    }

}