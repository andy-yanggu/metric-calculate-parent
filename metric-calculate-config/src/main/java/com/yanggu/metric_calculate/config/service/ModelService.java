package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.base.domain.vo.PageVO;
import com.yanggu.metric_calculate.config.domain.dto.ModelDTO;
import com.yanggu.metric_calculate.config.domain.entity.ModelEntity;
import com.yanggu.metric_calculate.config.domain.query.ModelQuery;
import com.yanggu.metric_calculate.config.domain.vo.ModelVO;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;

import java.util.List;

/**
 * 数据明细宽表 服务层
 */
public interface ModelService extends IService<ModelEntity> {

    /**
     * 新增宽表
     *
     * @param modelDto
     */
    void saveData(ModelDTO modelDto) throws Exception;

    /**
     * 根据主键更新
     *
     * @param modelDto
     */
    void updateData(ModelDTO modelDto);

    /**
     * 修改其他数据
     */
    void updateOtherData(ModelDTO modelDto);

    void deleteById(Integer id);

    List<ModelVO> listData(ModelQuery req);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    ModelVO queryById(Integer id);

    PageVO<ModelVO> pageData(ModelQuery req);

    Model toCoreModel(Integer modelId);

    List<Model> getAllCoreModel();

}
