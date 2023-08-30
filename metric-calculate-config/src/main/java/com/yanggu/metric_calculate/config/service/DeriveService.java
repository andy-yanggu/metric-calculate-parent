package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.Derive;

/**
 * 派生指标 服务层。
 */
public interface DeriveService extends IService<Derive> {

    /**
     * 新增派生指标
     *
     * @param deriveDto
     */
    void create(DeriveDto deriveDto) throws Exception;

    /**
     * 根据id删除
     *
     * @param id
     */
    void deleteById(Integer id);

    DeriveDto queryById(Integer id);

}