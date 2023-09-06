package com.yanggu.metric_calculate.config.pojo.req;

import lombok.Data;

@Data
public class DimensionQueryReq {

    /**
     * 维度名称
     */
    private String dimensionName;

    /**
     * 维度中文名
     */
    private String dimensionDisplayName;

    /**
     * 排序字段名
     */
    private String orderByColumnName;

    /**
     * 是否升序
     */
    private Boolean asc;

}
