package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.FieldOrderParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import com.yanggu.metric_calculate.config.pojo.entity.FieldOrderParam;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.FieldOrderParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字段排序配置类 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class FieldOrderParamServiceImpl extends ServiceImpl<FieldOrderParamMapper, FieldOrderParam> implements FieldOrderParamService {

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(FieldOrderParam fieldOrderParam) throws Exception {
        AviatorExpressParam aviatorExpressParam = fieldOrderParam.getAviatorExpressParam();
        aviatorExpressParamService.saveData(aviatorExpressParam);
        fieldOrderParam.setAviatorExpressParamId(aviatorExpressParam.getId());
        super.save(fieldOrderParam);
    }

}