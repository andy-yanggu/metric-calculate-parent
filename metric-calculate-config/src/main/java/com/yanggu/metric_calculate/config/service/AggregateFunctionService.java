package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionDto;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunction;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 聚合函数 服务层。
 */
public interface AggregateFunctionService extends IService<AggregateFunction> {

    void saveData(AggregateFunctionDto aggregateFunction) throws Exception;

    AggregateFunctionDto queryById(Integer id);

    List<AggregateFunctionDto> listData();

    void jarSave(MultipartFile file) throws Exception;

}