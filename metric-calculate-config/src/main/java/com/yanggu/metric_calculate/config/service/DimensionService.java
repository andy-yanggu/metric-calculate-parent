package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.base.domain.vo.PageVO;
import com.yanggu.metric_calculate.config.domain.dto.DimensionDTO;
import com.yanggu.metric_calculate.config.domain.entity.DimensionEntity;
import com.yanggu.metric_calculate.config.domain.query.DimensionQuery;
import com.yanggu.metric_calculate.config.domain.vo.DimensionVO;

import java.util.List;

/**
 * 维度表 服务层。
 */
public interface DimensionService extends IService<DimensionEntity> {

    void saveData(DimensionDTO dimensionDto);

    void updateData(DimensionDTO dimensionDto);

    void deleteById(Integer id);

    List<DimensionVO> listData(DimensionQuery req);

    DimensionVO queryById(Integer id);

    PageVO<DimensionVO> pageData(DimensionQuery req);

}