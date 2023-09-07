package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionInstanceDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstance;
import com.yanggu.metric_calculate.config.pojo.req.AviatorFunctionInstanceQueryReq;

import java.util.List;

/**
 * Aviator函数实例 服务层。
 */
public interface AviatorFunctionInstanceService extends IService<AviatorFunctionInstance> {

    void saveData(AviatorFunctionInstanceDto aviatorFunctionInstanceDto);

    void updateData(AviatorFunctionInstanceDto aviatorFunctionInstanceDto);

    void deleteById(Integer id);

    List<AviatorFunctionInstanceDto> listData(AviatorFunctionInstanceQueryReq req);

    AviatorFunctionInstanceDto queryById(Integer id);

    Page<AviatorFunctionInstanceDto> pageData(Integer pageNumber, Integer pageSize, AviatorFunctionInstanceQueryReq req);

}