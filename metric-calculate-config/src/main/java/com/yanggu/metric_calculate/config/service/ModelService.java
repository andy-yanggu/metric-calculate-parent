package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDTO;
import com.yanggu.metric_calculate.config.pojo.entity.ModelEntity;
import com.yanggu.metric_calculate.config.pojo.query.ModelQuery;

import java.util.List;

/**
 * 数据明细宽表 服务层
 */
public interface ModelService extends IService<ModelEntity> {

    /**
     * 新增宽表
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

    List<ModelDTO> listData(ModelQuery req);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    ModelDTO queryById(Integer id);

    Page<ModelDTO> pageData(Integer pageNumber, Integer pageSize, ModelQuery req);

    com.yanggu.metric_calculate.core.pojo.data_detail_table.Model toCoreModel(Integer modelId);

    List<com.yanggu.metric_calculate.core.pojo.data_detail_table.Model> getAllCoreModel();

}