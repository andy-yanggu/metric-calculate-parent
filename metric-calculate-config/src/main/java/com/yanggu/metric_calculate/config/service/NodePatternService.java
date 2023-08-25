package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.NodePattern;

/**
 * CEP匹配配置数据 服务层。
 */
public interface NodePatternService extends IService<NodePattern> {

    void saveData(NodePattern nodePattern) throws Exception;

}
