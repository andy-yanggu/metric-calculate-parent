package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.DimensionDTO;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionEntity;
import com.yanggu.metric_calculate.config.pojo.query.DimensionQuery;

import java.util.List;

/**
 * 维度表 服务层。
 */
public interface DimensionService extends IService<DimensionEntity> {

    void saveData(DimensionDTO dimensionDto);

    void updateData(DimensionDTO dimensionDto);

    void deleteById(Integer id);

    List<DimensionDTO> listData(DimensionQuery req);

    DimensionDTO queryById(Integer id);

    Page<DimensionDTO> pageData(Integer pageNumber, Integer pageSize, DimensionQuery req);

}