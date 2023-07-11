package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import com.yanggu.metric_calculate.config.pojo.entity.Model;

/**
 * 数据明细宽表 服务层
 */
public interface ModelService extends IService<Model> {

    void create(ModelDto modelDto);

    ModelDto queryById(Integer id);

}