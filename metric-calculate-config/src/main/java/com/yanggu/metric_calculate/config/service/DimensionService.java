package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.DimensionDto;
import com.yanggu.metric_calculate.config.pojo.entity.Dimension;
import com.yanggu.metric_calculate.config.pojo.req.DimensionQueryReq;

import java.util.List;

/**
 * 维度表 服务层。
 */
public interface DimensionService extends IService<Dimension> {

    void saveData(DimensionDto dimensionDto);

    void updateData(DimensionDto dimensionDto);

    void deleteById(Integer id);

    List<DimensionDto> listData(DimensionQueryReq req);

    DimensionDto queryById(Integer id);

    Page<DimensionDto> pageData(Integer pageNumber, Integer pageSize, DimensionQueryReq req);

}