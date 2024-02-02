package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.base.vo.PageVO;
import com.yanggu.metric_calculate.config.pojo.dto.AtomDTO;
import com.yanggu.metric_calculate.config.pojo.entity.AtomEntity;
import com.yanggu.metric_calculate.config.pojo.query.AtomQuery;
import com.yanggu.metric_calculate.config.pojo.vo.AtomVO;

import java.util.List;

/**
 * 原子指标 服务层。
 */
public interface AtomService extends IService<AtomEntity> {

    /**
     * 新增原子指标
     *
     * @param atomDto
     */
    void saveData(AtomDTO atomDto) throws Exception;

    /**
     * 修改原子指标
     *
     * @param atomDto
     * @throws Exception
     */
    void updateData(AtomDTO atomDto) throws Exception;

    /**
     * 根据id删除
     *
     * @param id
     */
    void deleteById(Integer id);

    AtomVO queryById(Integer id);

    List<AtomVO> listData(AtomQuery atomQuery);

    PageVO<AtomVO> pageQuery(AtomQuery atomQuery);

}