package com.yanggu.metric_calculate.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.ModelColumnMapper;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.pojo.exception.BusinessException;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.ModelColumnAviatorExpressRelationService;
import com.yanggu.metric_calculate.config.service.ModelColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.enums.ModelColumnFieldType.VIRTUAL;
import static com.yanggu.metric_calculate.config.enums.ResultCode.*;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelColumnTableDef.MODEL_COLUMN;

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
    public void saveModelColumnList(List<ModelColumn> modelColumnList) {
        //校验名称或者中文名是否重复
        checkModelColumList(modelColumnList);
        //批量保存宽表字段
        saveBatch(modelColumnList);
        modelColumnList.forEach(modelColumn -> {
            //如果是虚拟字段, 保存Aviator表达式和中间表数据
            if (VIRTUAL.name().equals(modelColumn.getFieldType())) {
                AviatorExpressParam aviatorExpressParam = modelColumn.getAviatorExpressParam();
                aviatorExpressParamService.save(aviatorExpressParam);

                ModelColumnAviatorExpressRelation relation = new ModelColumnAviatorExpressRelation();
                relation.setModelColumnId(modelColumn.getId());
                relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                relation.setUserId(modelColumn.getUserId());
                modelColumnAviatorExpressRelationService.save(relation);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateModelColumnList(Model model) {
        List<ModelColumn> updateModelColumnList = model.getModelColumnList();
        //校验名称或者中文名是否重复
        checkModelColumList(updateModelColumnList);

        QueryWrapper modelIdQuery = QueryWrapper.create().where(MODEL_COLUMN.MODEL_ID.eq(model.getId()));
        //根据modelId查询ModelColumn
        List<ModelColumn> oldModelColumnList = modelColumnMapper.selectListByQuery(modelIdQuery);
        //维度字段
        List<ModelDimensionColumn> modelDimensionColumnList = model.getModelDimensionColumnList();
        //时间字段
        List<ModelTimeColumn> modelTimeColumnList = model.getModelTimeColumnList();
        //过滤出删除的字段
        List<Integer> updateIdList = updateModelColumnList.stream()
                .map(ModelColumn::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Integer> oldIdList = oldModelColumnList.stream()
                .map(ModelColumn::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Integer> deleteIdList = CollUtil.subtractToList(oldIdList, updateIdList);
        if (CollUtil.isNotEmpty(deleteIdList)) {

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