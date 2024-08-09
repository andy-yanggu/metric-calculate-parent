package com.yanggu.metric_calculate.config.domain.query;

import com.yanggu.metric_calculate.config.base.domain.query.PageQuery;
import com.yanggu.metric_calculate.config.domain.entity.DeriveEntity;
import com.yanggu.metric_calculate.config.enums.WindowTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeriveQuery extends PageQuery<DeriveEntity> {

    @Serial
    private static final long serialVersionUID = 4812775715360708181L;

    private String deriveName;

    private String deriveDisplayName;

    private String modelName;

    private String modelDisplayName;

    private String aggregateFunctionName;

    private String timeFormat;

    private String timeColumnName;

    private String timeColumnDisplayName;

    private String dimensionColumnName;

    private String dimensionColumnDisplayName;

    private String dimensionName;

    private String dimensionDisplayName;

    private WindowTypeEnum windowType;

}
