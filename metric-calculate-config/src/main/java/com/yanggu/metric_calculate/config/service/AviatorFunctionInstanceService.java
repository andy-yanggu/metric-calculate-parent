package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.base.vo.PageVO;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionInstanceDTO;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstanceEntity;
import com.yanggu.metric_calculate.config.pojo.query.AviatorFunctionInstanceQuery;
import com.yanggu.metric_calculate.config.pojo.vo.AviatorFunctionInstanceVO;

import java.util.List;

/**
 * Aviator函数实例 服务层。
 */
public interface AviatorFunctionInstanceService extends IService<AviatorFunctionInstanceEntity> {

    void saveData(AviatorFunctionInstanceDTO aviatorFunctionInstanceDto);

    void updateData(AviatorFunctionInstanceDTO aviatorFunctionInstanceDto);

    void deleteById(Integer id);

    List<AviatorFunctionInstanceVO> listData(AviatorFunctionInstanceQuery req);

    AviatorFunctionInstanceVO queryById(Integer id);

    PageVO<AviatorFunctionInstanceVO> pageData(AviatorFunctionInstanceQuery req);

}