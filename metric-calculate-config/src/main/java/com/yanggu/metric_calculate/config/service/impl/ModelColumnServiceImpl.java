package com.yanggu.metric_calculate.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.ModelColumnMapper;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumnAviatorExpressRelation;
import com.yanggu.metric_calculate.config.pojo.exception.BusinessException;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.ModelColumnAviatorExpressRelationService;
import com.yanggu.metric_calculate.config.service.ModelColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.enums.ModelColumnFieldType.VIRTUAL;
import static com.yanggu.metric_calculate.config.enums.ResultCode.MODEL_COLUMN_EMPTY;

/**
 * 宽表字段 服务层实现。
 */
@Service
public class ModelColumnServiceImpl extends ServiceImpl<ModelColumnMapper, ModelColumn> implements ModelColumnService {

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private ModelColumnAviatorExpressRelationService modelColumnAviatorExpressRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveModelColumnList(List<ModelColumn> modelColumnList) {
        if (CollUtil.isEmpty(modelColumnList)) {
            throw new BusinessException(MODEL_COLUMN_EMPTY);
        }
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

}