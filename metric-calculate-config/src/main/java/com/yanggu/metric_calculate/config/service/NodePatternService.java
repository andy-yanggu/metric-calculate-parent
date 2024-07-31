package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.domain.entity.ModelColumnEntity;
import com.yanggu.metric_calculate.config.domain.entity.NodePatternEntity;

import java.util.List;

/**
 * CEP匹配配置数据 服务层。
 */
public interface NodePatternService extends IService<NodePatternEntity> {

    void saveData(NodePatternEntity nodePattern, List<ModelColumnEntity> modelColumnList) throws Exception;

    void deleteData(NodePatternEntity nodePattern);

}
