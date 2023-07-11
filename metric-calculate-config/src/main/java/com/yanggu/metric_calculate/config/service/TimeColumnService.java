package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.ModelColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.TimeColumn;

import java.util.List;

/**
 * 时间字段 服务层。
 */
public interface TimeColumnService extends IService<TimeColumn> {

    void saveTimeColumn(List<ModelColumnDto> modelColumnDtoList);

}