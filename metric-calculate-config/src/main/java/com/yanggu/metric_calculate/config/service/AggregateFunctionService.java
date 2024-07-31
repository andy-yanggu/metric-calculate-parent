package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.base.domain.vo.PageVO;
import com.yanggu.metric_calculate.config.domain.dto.AggregateFunctionDTO;
import com.yanggu.metric_calculate.config.domain.entity.AggregateFunctionEntity;
import com.yanggu.metric_calculate.config.domain.query.AggregateFunctionQuery;
import com.yanggu.metric_calculate.config.domain.vo.AggregateFunctionVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 聚合函数 服务层。
 */
public interface AggregateFunctionService extends IService<AggregateFunctionEntity> {

    void saveData(AggregateFunctionDTO aggregateFunctionDto) throws Exception;

    void jarSave(MultipartFile file) throws Exception;

    void updateData(AggregateFunctionDTO aggregateFunctionDto);

    void deleteById(Integer id);

    List<AggregateFunctionVO> listData(AggregateFunctionQuery queryReq);

    AggregateFunctionVO queryById(Integer id);

    PageVO<AggregateFunctionVO> pageQuery(AggregateFunctionQuery queryReq);

}