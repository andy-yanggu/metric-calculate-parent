package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import com.yanggu.metric_calculate.config.pojo.entity.NodePattern;

import java.util.List;

/**
 * CEP匹配配置数据 服务层。
 */
public interface NodePatternService extends IService<NodePattern> {

    void saveData(NodePattern nodePattern, List<ModelColumn> modelColumnList) throws Exception;

    void deleteData(NodePattern nodePattern);

}
