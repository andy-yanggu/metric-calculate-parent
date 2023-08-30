package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.WindowParam;

/**
 * 窗口相关参数 服务层。
 */
public interface WindowParamService extends IService<WindowParam> {

    void saveData(WindowParam windowParam) throws Exception;

    void deleteData(WindowParam windowParam);

}