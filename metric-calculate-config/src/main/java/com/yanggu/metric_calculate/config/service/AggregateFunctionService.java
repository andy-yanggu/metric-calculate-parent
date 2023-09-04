package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionDto;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunction;
import com.yanggu.metric_calculate.config.pojo.req.AggregateFunctionQueryReq;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 聚合函数 服务层。
 */
public interface AggregateFunctionService extends IService<AggregateFunction> {

    void saveData(AggregateFunctionDto aggregateFunction) throws Exception;

    void jarSave(MultipartFile file) throws Exception;

    void updateData(AggregateFunctionDto aggregateFunctionDto);

    void deleteById(Integer id);

    List<AggregateFunctionDto> listData(AggregateFunctionQueryReq queryReq);

    AggregateFunctionDto queryById(Integer id);

    Page<AggregateFunctionDto> pageQuery(Integer pageNumber, Integer pageSize, AggregateFunctionQueryReq queryReq);

}