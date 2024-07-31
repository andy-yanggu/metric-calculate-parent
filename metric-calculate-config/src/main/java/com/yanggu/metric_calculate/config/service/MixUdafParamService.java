package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.domain.entity.MixUdafParamEntity;
import com.yanggu.metric_calculate.config.domain.entity.ModelColumnEntity;

import java.util.List;

/**
 * 混合类型udaf参数 服务层。
 */
public interface MixUdafParamService extends IService<MixUdafParamEntity> {

    void saveData(MixUdafParamEntity mixUdafParam, List<ModelColumnEntity> modelColumnList) throws Exception;

    void deleteData(MixUdafParamEntity mixUdafParam);

}