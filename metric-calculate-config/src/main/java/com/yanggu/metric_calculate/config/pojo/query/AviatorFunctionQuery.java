package com.yanggu.metric_calculate.config.pojo.query;

import com.yanggu.metric_calculate.config.base.query.PageQuery;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AviatorFunctionQuery extends PageQuery<AviatorFunctionEntity> {

    /**
     * Aviator函数名称
     */
    private String aviatorFunctionName;

    /**
     * Aviator函数中文名
     */
    private String aviatorFunctionDisplayName;

    /**
     * 排序字段名
     */
    private String orderByColumnName;

    /**
     * 是否升序
     */
    private Boolean asc;

}
