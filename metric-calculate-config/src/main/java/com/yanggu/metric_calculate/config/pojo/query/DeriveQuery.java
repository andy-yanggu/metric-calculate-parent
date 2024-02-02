package com.yanggu.metric_calculate.config.pojo.query;

import com.yanggu.metric_calculate.config.base.query.PageQuery;
import com.yanggu.metric_calculate.config.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.config.pojo.entity.DeriveEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeriveQuery extends PageQuery<DeriveEntity> {

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
