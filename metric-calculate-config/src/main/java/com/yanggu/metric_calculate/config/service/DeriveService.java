package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.Derive;
import com.yanggu.metric_calculate.config.pojo.req.DeriveQueryReq;
import com.yanggu.metric_calculate.config.pojo.vo.DeriveMetricsConfigData;

import java.util.List;

/**
 * 派生指标 服务层。
 */
public interface DeriveService extends IService<Derive> {

    /**
     * 新增派生指标
     *
     * @param deriveDto
     */
    void saveData(DeriveDto deriveDto) throws Exception;

    /**
     * 修改派生指标
     *
     * @param deriveDto
     * @throws Exception
     */
    void updateData(DeriveDto deriveDto) throws Exception;

    /**
     * 根据id删除
     *
     * @param id
     */
    void deleteById(Integer id);

    DeriveDto queryById(Integer id);

    List<DeriveDto> listData(DeriveQueryReq deriveQuery);

    Page<DeriveDto> pageQuery(Integer pageNumber, Integer pageSize, DeriveQueryReq deriveQuery);

    List<DeriveMetricsConfigData> getAllCoreDeriveMetrics();

}