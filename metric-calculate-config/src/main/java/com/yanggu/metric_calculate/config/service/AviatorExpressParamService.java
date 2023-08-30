package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;

/**
 * Aviator表达式配置 服务层。
 */
public interface AviatorExpressParamService extends IService<AviatorExpressParam> {

    /**
     * <p>1. 保存表达式</p>
     * <p>2. 宽表字段依赖关系</p>
     * <p>3. 保存依赖的Aviator函数实例</>
     * @param aviatorExpressParam
     * @throws Exception
     */
    void saveDataByModelColumn(AviatorExpressParam aviatorExpressParam) throws Exception;

    /**
     * <p>1. 保存表达式</p>
     * <p>2. MixUdafParam实例依赖关系</p>
     * <p>3. 保存依赖的Aviator函数实例</>
     * @param aviatorExpressParam
     * @throws Exception
     */
    void saveDataByMixUdafParamItem(AviatorExpressParam aviatorExpressParam) throws Exception;

    void deleteData(AviatorExpressParam aviatorExpressParam);

}