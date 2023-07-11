package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.ModelColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;

import java.util.List;

/**
 * 宽表字段 服务层。
 */
public interface ModelColumnService extends IService<ModelColumn> {

    void insertModelColumnList(List<ModelColumnDto> modelColumnDtoList);

}