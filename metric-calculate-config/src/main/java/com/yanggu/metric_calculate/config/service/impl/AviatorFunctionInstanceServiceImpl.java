package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.AviatorFunctionInstanceMapper;
import com.yanggu.metric_calculate.config.mapstruct.AviatorFunctionInstanceMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionInstanceDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstance;
import com.yanggu.metric_calculate.config.service.AviatorFunctionInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Aviator函数实例 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class AviatorFunctionInstanceServiceImpl extends ServiceImpl<AviatorFunctionInstanceMapper, AviatorFunctionInstance> implements AviatorFunctionInstanceService {

    @Autowired
    private AviatorFunctionInstanceMapstruct aviatorFunctionInstanceMapstruct;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(AviatorFunctionInstanceDto aviatorFunctionInstanceDto) {
        AviatorFunctionInstance aviatorFunctionInstance = aviatorFunctionInstanceMapstruct.toEntity(aviatorFunctionInstanceDto);

    }

}