package com.yanggu.metric_calculate.config.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDTO;
import com.yanggu.metric_calculate.config.pojo.entity.DeriveEntity;
import com.yanggu.metric_calculate.config.pojo.query.DeriveQuery;
import com.yanggu.metric_calculate.config.pojo.vo.DeriveMetricsConfigData;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;

import java.util.List;

/**
 * 派生指标 服务层。
 */
public interface DeriveService extends IService<DeriveEntity> {

    /**
     * 新增派生指标
     *
     * @param deriveDto
     */
    void saveData(DeriveDTO deriveDto) throws Exception;

    /**
     * 修改派生指标
     *
     * @param deriveDto
     * @throws Exception
     */
    void updateData(DeriveDTO deriveDto) throws Exception;

    /**
     * 根据id删除
     *
     * @param id
     */
    void deleteById(Integer id);

    DeriveDTO queryById(Integer id);

    List<DeriveDTO> listData(DeriveQuery deriveQuery);

    Page<DeriveDTO> pageQuery(Integer pageNumber, Integer pageSize, DeriveQuery deriveQuery);

    DeriveMetrics toCoreDeriveMetrics(Integer deriveId);

    List<DeriveMetricsConfigData> getAllCoreDeriveMetrics();

}