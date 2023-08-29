package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.AviatorFunctionMapper;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunction;
import com.yanggu.metric_calculate.config.service.AviatorFunctionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Aviator函数 服务层实现。
 */
@Service
public class AviatorFunctionServiceImpl extends ServiceImpl<AviatorFunctionMapper, AviatorFunction> implements AviatorFunctionService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void jarSave(MultipartFile file) {

    }

}