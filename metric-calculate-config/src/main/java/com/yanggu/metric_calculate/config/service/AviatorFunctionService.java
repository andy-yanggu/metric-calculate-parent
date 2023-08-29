package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunction;
import org.springframework.web.multipart.MultipartFile;

/**
 * Aviator函数 服务层。
 */
public interface AviatorFunctionService extends IService<AviatorFunction> {

    void jarSave(MultipartFile file) throws Exception;

    void saveData(AviatorFunctionDto aviatorFunctionDto);

}