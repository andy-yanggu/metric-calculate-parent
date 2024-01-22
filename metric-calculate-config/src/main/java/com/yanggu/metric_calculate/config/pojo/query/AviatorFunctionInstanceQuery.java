package com.yanggu.metric_calculate.config.pojo.query;

import com.yanggu.metric_calculate.config.base.query.PageQuery;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstanceEntity;
import lombok.Data;

@Data
public class AviatorFunctionInstanceQuery extends PageQuery<AviatorFunctionInstanceEntity> {

    /**
     * 宽表中文名
     */
    private String aviatorFunctionInstanceDisplayName;

}
