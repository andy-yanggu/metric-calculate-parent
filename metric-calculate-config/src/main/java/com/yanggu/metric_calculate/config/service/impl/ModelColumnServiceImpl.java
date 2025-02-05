package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.domain.entity.AviatorExpressParamEntity;
import com.yanggu.metric_calculate.config.domain.entity.ModelColumnAviatorExpressParamRelationEntity;
import com.yanggu.metric_calculate.config.domain.entity.ModelColumnEntity;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.ModelColumnMapper;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.ModelColumnAviatorExpressRelationService;
import com.yanggu.metric_calculate.config.service.ModelColumnService;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.yanggu.metric_calculate.config.enums.ModelColumnFieldType.VIRTUAL;
import static com.yanggu.metric_calculate.config.enums.ResultCode.*;

/**
 * 宽表字段 服务层实现。
 */
@Service
public class ModelColumnServiceImpl extends ServiceImpl<ModelColumnMapper, ModelColumnEntity> implements ModelColumnService {

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private ModelColumnAviatorExpressRelationService modelColumnAviatorExpressRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveModelColumnList(List<ModelColumnEntity> modelColumnList) throws Exception {
        //校验名称或者中文名是否重复
        checkModelColumList(modelColumnList);
        //批量保存宽表字段
        saveBatch(modelColumnList);
        //保存虚拟字段类型的宽表字段依赖的表达式
        for (ModelColumnEntity modelColumn : modelColumnList) {
            if (!VIRTUAL.equals(modelColumn.getFieldType())) {
                continue;
            }
            //如果是虚拟字段, 保存Aviator表达式和中间表数据
            AviatorExpressParamEntity aviatorExpressParam = modelColumn.getAviatorExpressParam();
            //设置所有的宽表字段
            aviatorExpressParam.setModelColumnList(modelColumnList);
            aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);

            ModelColumnAviatorExpressParamRelationEntity relation = new ModelColumnAviatorExpressParamRelationEntity();
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
    private void checkModelColumList(List<ModelColumnEntity> modelColumnList) {
        if (CollUtil.isEmpty(modelColumnList)) {
            throw new BusinessException(MODEL_COLUMN_EMPTY);
        }
        //校验宽表字段名是否重复
        Set<String> nameSet = new HashSet<>();
        Set<String> displayNameSet = new HashSet<>();
        for (ModelColumnEntity modelColumn : modelColumnList) {
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