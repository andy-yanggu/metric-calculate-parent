package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.base.domain.vo.PageVO;
import com.yanggu.metric_calculate.config.domain.dto.AviatorFunctionDTO;
import com.yanggu.metric_calculate.config.domain.entity.AviatorFunctionEntity;
import com.yanggu.metric_calculate.config.domain.query.AviatorFunctionQuery;
import com.yanggu.metric_calculate.config.domain.vo.AviatorFunctionVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Aviator函数 服务层。
 */
public interface AviatorFunctionService extends IService<AviatorFunctionEntity> {

    void saveData(AviatorFunctionDTO aviatorFunctionDto) throws Exception;

    void jarSave(MultipartFile file) throws Exception;

    void updateData(AviatorFunctionDTO aviatorFunctionDto);

    void deleteById(Integer id);

    List<AviatorFunctionVO> listData(AviatorFunctionQuery req);

    AviatorFunctionVO queryById(Integer id);

    PageVO<AviatorFunctionVO> pageData(AviatorFunctionQuery req);

}