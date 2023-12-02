package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.pojo.dto.AtomDto;
import com.yanggu.metric_calculate.config.pojo.entity.Atom;
import com.yanggu.metric_calculate.config.mapper.AtomMapper;
import com.yanggu.metric_calculate.config.pojo.req.AtomQueryReq;
import com.yanggu.metric_calculate.config.service.AtomService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 原子指标 服务层实现。
 */
@Service
public class AtomServiceImpl extends ServiceImpl<AtomMapper, Atom> implements AtomService {

    @Override
    public void saveData(AtomDto atomDto) throws Exception {

    }

    @Override
    public void updateData(AtomDto atomDto) throws Exception {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public AtomDto queryById(Integer id) {
        return null;
    }

    @Override
    public List<AtomDto> listData(AtomQueryReq atomQueryReq) {
        return null;
    }

    @Override
    public Page<AtomDto> pageQuery(Integer pageNumber, Integer pageSize, AtomQueryReq atomQueryReq) {
        return null;
    }

}
