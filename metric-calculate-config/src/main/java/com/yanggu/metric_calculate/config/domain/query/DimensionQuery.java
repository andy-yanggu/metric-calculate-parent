package com.yanggu.metric_calculate.config.domain.query;

import com.yanggu.metric_calculate.config.base.domain.query.PageQuery;
import com.yanggu.metric_calculate.config.domain.entity.DimensionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DimensionQuery extends PageQuery<DimensionEntity> {

    @Serial
    private static final long serialVersionUID = 7352665037617357988L;

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
