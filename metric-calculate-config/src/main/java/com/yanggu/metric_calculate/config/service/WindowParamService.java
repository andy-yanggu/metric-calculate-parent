package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.WindowParam;

/**
 * 窗口相关参数 服务层。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
public interface WindowParamService extends IService<WindowParam> {

    void saveData(WindowParam windowParam);

}