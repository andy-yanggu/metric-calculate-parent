package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionInstanceDTO;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstanceEntity;
import com.yanggu.metric_calculate.config.pojo.query.AviatorFunctionInstanceQuery;

import java.util.List;

/**
 * Aviator函数实例 服务层。
 */
public interface AviatorFunctionInstanceService extends IService<AviatorFunctionInstanceEntity> {

    void saveData(AviatorFunctionInstanceDTO aviatorFunctionInstanceDto);

    void updateData(AviatorFunctionInstanceDTO aviatorFunctionInstanceDto);

    void deleteById(Integer id);

    List<AviatorFunctionInstanceDTO> listData(AviatorFunctionInstanceQuery req);

    AviatorFunctionInstanceDTO queryById(Integer id);

    Page<AviatorFunctionInstanceDTO> pageData(Integer pageNumber, Integer pageSize, AviatorFunctionInstanceQuery req);

}