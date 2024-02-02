package com.yanggu.metric_calculate.config.pojo.query;

import com.yanggu.metric_calculate.config.base.query.PageQuery;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class AviatorFunctionQuery extends PageQuery<AviatorFunctionEntity> {

    @Serial
    private static final long serialVersionUID = -3246668601927775013L;

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
