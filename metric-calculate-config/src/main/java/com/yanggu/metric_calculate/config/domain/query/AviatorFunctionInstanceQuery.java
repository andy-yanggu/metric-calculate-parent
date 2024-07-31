package com.yanggu.metric_calculate.config.domain.query;

import com.yanggu.metric_calculate.config.base.domain.query.PageQuery;
import com.yanggu.metric_calculate.config.domain.entity.AviatorFunctionInstanceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class AviatorFunctionInstanceQuery extends PageQuery<AviatorFunctionInstanceEntity> {

    @Serial
    private static final long serialVersionUID = -549643225672010915L;

    /**
     * 宽表中文名
     */
    private String aviatorFunctionInstanceDisplayName;

}
