package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.ModelColumnMapper;
import com.yanggu.metric_calculate.config.mapstruct.AviatorExpressParamMapstruct;
import com.yanggu.metric_calculate.config.mapstruct.ModelColumnMapstruct;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumnAviatorExpressRelation;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.ModelColumnAviatorExpressRelationService;
import com.yanggu.metric_calculate.config.service.ModelColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.enums.ModelColumnFieldType.VIRTUAL;

/**
 * 宽表字段 服务层实现。
 */
@Service
public class ModelColumnServiceImpl extends ServiceImpl<ModelColumnMapper, ModelColumn> implements ModelColumnService {

    @Autowired
    private ModelColumnMapstruct modelColumnMapstruct;

    @Autowired
    private ModelColumnService modelColumnService;

    @Autowired
    private AviatorExpressParamMapstruct aviatorExpressParamMapstruct;

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private ModelColumnAviatorExpressRelationService modelColumnAviatorExpressRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveModelColumnList(List<ModelColumn> modelColumnList) {
        //新增宽表字段
        modelColumnList.forEach(modelColumn -> {
            modelColumnService.save(modelColumn);
            //如果是虚拟字段, 保存Aviator表达式和中间表数据
            if (VIRTUAL.name().equals(modelColumn.getFieldType())) {
                AviatorExpressParam aviatorExpressParam = modelColumn.getAviatorExpressParam();
                aviatorExpressParamService.save(aviatorExpressParam);

                ModelColumnAviatorExpressRelation relation = new ModelColumnAviatorExpressRelation();
                relation.setModelColumnId(modelColumn.getId());
                relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                modelColumnAviatorExpressRelationService.save(relation);
            }
        });
    }

}