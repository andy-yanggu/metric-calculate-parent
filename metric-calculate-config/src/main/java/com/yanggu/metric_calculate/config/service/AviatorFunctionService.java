package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunction;
import com.yanggu.metric_calculate.config.pojo.req.AviatorFunctionQueryReq;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Aviator函数 服务层。
 */
public interface AviatorFunctionService extends IService<AviatorFunction> {

    void saveData(AviatorFunctionDto aviatorFunctionDto) throws Exception;

    void jarSave(MultipartFile file) throws Exception;

    void updateData(AviatorFunctionDto aviatorFunctionDto);

    void deleteById(Integer id);

    List<AviatorFunctionDto> listData(AviatorFunctionQueryReq req);

    AviatorFunctionDto queryById(Integer id);

    Page<AviatorFunctionDto> pageData(Integer pageNumber, Integer pageSize, AviatorFunctionQueryReq req);

}