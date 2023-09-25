package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParam;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;

import java.util.List;

/**
 * 混合类型udaf参数 服务层。
 */
public interface MixUdafParamService extends IService<MixUdafParam> {

    void saveData(MixUdafParam mixUdafParam, List<ModelColumn> modelColumnList) throws Exception;

    void deleteData(MixUdafParam mixUdafParam);

}