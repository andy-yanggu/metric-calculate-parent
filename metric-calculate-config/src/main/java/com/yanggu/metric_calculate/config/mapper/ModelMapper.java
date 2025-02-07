package com.yanggu.metric_calculate.config.mapper;

import com.mybatisflex.core.BaseMapper;
import com.yanggu.metric_calculate.config.domain.entity.ModelEntity;
import org.springframework.stereotype.Repository;

/**
 * 数据明细宽表 映射层。
 */
@Repository
public interface ModelMapper extends BaseMapper<ModelEntity> {
}
