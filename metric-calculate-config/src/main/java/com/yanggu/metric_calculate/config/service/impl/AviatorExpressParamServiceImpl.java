package com.yanggu.metric_calculate.config.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorExpressParamDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import com.yanggu.metric_calculate.config.mapper.AviatorExpressParamMapper;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import org.springframework.stereotype.Service;

/**
 * Aviator表达式配置 服务层实现。
 */
@Service
public class AviatorExpressParamServiceImpl extends ServiceImpl<AviatorExpressParamMapper, AviatorExpressParam> implements AviatorExpressParamService {

    @Override
    public boolean checkAviatorExpress(AviatorExpressParamDto aviatorExpressParamDto) {
        if (aviatorExpressParamDto == null) {
            return false;
        }
        String express = aviatorExpressParamDto.getExpress();
        if (StrUtil.isBlank(express)) {
            return false;
        }

        return false;
    }

}