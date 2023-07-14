package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import com.yanggu.metric_calculate.config.pojo.entity.Model;

/**
 * 数据明细宽表 服务层
 */
public interface ModelService extends IService<Model> {

    /**
     * 新增宽表
     * @param modelDto
     */
    void create(ModelDto modelDto);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    ModelDto queryById(Integer id);

}