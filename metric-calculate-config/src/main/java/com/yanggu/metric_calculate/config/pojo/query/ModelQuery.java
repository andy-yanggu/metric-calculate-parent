package com.yanggu.metric_calculate.config.pojo.query;

import com.yanggu.metric_calculate.config.base.query.PageQuery;
import com.yanggu.metric_calculate.config.pojo.entity.ModelEntity;
import lombok.Data;

@Data
public class ModelQuery extends PageQuery<ModelEntity> {

    /**
     * 宽表名称
     */
    private String modelName;

    /**
     * 宽表中文名
     */
    private String modelDisplayName;

    /**
     * 排序字段名
     */
    private String orderByColumnName;

    /**
     * 是否升序
     */
    private Boolean asc;

}
