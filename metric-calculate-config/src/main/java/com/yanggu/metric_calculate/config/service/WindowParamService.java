package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumnEntity;
import com.yanggu.metric_calculate.config.pojo.entity.WindowParamEntity;

import java.util.List;

/**
 * 窗口相关参数 服务层。
 */
public interface WindowParamService extends IService<WindowParamEntity> {

    void saveData(WindowParamEntity windowParam, List<ModelColumnEntity> modelColumnList) throws Exception;

    void deleteData(WindowParamEntity windowParam);

}