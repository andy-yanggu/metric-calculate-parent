package com.yanggu.metric_calculate.config.pojo.query;

import com.yanggu.metric_calculate.config.base.query.PageQuery;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionEntity;
import lombok.Data;

import java.util.List;

@Data
public class DimensionQuery extends PageQuery<DimensionEntity> {

    /**
     * 维度名称
     */
    private String dimensionName;

    /**
     * 维度中文名
     */
    private String dimensionDisplayName;

    /**
     * 查询的id列表
     */
    private List<Integer> idList;

    /**
     * 排序字段名
     */
    private String orderByColumnName;

    /**
     * 是否升序
     */
    private Boolean asc;

}
