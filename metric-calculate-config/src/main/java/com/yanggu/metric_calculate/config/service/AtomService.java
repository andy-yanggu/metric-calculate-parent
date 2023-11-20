package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.AtomDto;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.Atom;
import com.yanggu.metric_calculate.config.pojo.req.AtomQueryReq;
import com.yanggu.metric_calculate.config.pojo.req.DeriveQueryReq;

import java.util.List;

/**
 * 原子指标 服务层。
 */
public interface AtomService extends IService<Atom> {

    /**
     * 新增原子指标
     *
     * @param atomDto
     */
    void saveData(AtomDto atomDto) throws Exception;

    /**
     * 修改原子指标
     *
     * @param atomDto
     * @throws Exception
     */
    void updateData(AtomDto atomDto) throws Exception;

    /**
     * 根据id删除
     *
     * @param id
     */
    void deleteById(Integer id);

    AtomDto queryById(Integer id);

    List<AtomDto> listData(AtomQueryReq atomQueryReq);

    Page<AtomDto> pageQuery(Integer pageNumber, Integer pageSize, AtomQueryReq atomQueryReq);

}