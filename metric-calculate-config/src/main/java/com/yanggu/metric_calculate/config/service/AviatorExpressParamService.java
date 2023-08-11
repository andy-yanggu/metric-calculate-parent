package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;

/**
 * Aviator表达式配置 服务层。
 */
public interface AviatorExpressParamService extends IService<AviatorExpressParam> {

    void saveData(AviatorExpressParam aviatorExpressParam) throws Exception;

    boolean checkAviatorExpress(AviatorExpressParam aviatorExpressParam) throws Exception;

}