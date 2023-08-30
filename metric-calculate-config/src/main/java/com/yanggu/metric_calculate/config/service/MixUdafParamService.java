package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParam;

/**
 * 混合类型udaf参数 服务层。
 */
public interface MixUdafParamService extends IService<MixUdafParam> {

    void saveData(MixUdafParam mixUdafParam) throws Exception;

    void deleteData(MixUdafParam mixUdafParam);

}